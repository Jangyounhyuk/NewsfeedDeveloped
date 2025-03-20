package com.example.developednewsfeed.user.service;

import com.example.developednewsfeed.common.config.PasswordEncoder;
import com.example.developednewsfeed.common.exception.ApplicationException;
import com.example.developednewsfeed.common.exception.ErrorCode;
import com.example.developednewsfeed.post.entity.Post;
import com.example.developednewsfeed.user.dto.request.ChangePasswordRequestDto;
import com.example.developednewsfeed.user.dto.request.UserDeleteRequestDto;
import com.example.developednewsfeed.user.dto.request.UserRestoreRequestDto;
import com.example.developednewsfeed.user.dto.request.UserUpdateRequestDto;
import com.example.developednewsfeed.user.dto.response.UserResponseDto;
import com.example.developednewsfeed.user.entity.User;
import com.example.developednewsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto save(String email, String password, String selfIntroduction) {

        if (userRepository.existsByEmail(email)) {
            throw new ApplicationException(ErrorCode.ALREADY_EXIST_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .selfIntroduction(selfIntroduction)
                .build();
        userRepository.save(user);

        return UserResponseDto.of(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto findByEmail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER)
        );

        return UserResponseDto.of(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto get(Long userId) {

        // activeUserFilter 필터 활성화 메서드
        userRepository.enableSoftDeleteFilter();

        // findById 는 필터를 무시하고 조회가 되기 때문에 별도의 메소드 정의 필요
        User user = userRepository.findByIdWithFilter(userId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER)
        );

        return UserResponseDto.of(user);
    }

    @Transactional
    public UserResponseDto update(Long id, UserUpdateRequestDto requestDto) {

        User user = userRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER)
        );

        user.update(requestDto.getSelfIntroduction());

        return UserResponseDto.of(user);
    }

    @Transactional
    public void changePassword(Long id, ChangePasswordRequestDto requestDto) {

        User user = userRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER)
        );

        // 변경하려는 비밀번호가 현재 비밀번호와 같은 경우
        if (passwordEncoder.matches(requestDto.getNewPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.SAME_PASSWORD);
        }

        // 비밀번호가 틀렸을 경우
        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.MISMATCHED_PASSWORD);
        }

        user.changePassword(passwordEncoder.encode(requestDto.getNewPassword()));
    }

    @Transactional
    public void delete(Long id, UserDeleteRequestDto requestDto) {

        User user = userRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER)
        );

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.MISMATCHED_PASSWORD);
        }

        user.changeDeletedAt(LocalDateTime.now());
    }

    // 삭제된 사용자 복구
    @Transactional
    public void restore(Long id, UserRestoreRequestDto requestDto) {

        User user = userRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER)
        );

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.MISMATCHED_PASSWORD);
        }

        user.changeDeletedAt(null);
    }

    // 삭제된 사용자 2주 지난 후 물리 삭제
    @Transactional
    @Scheduled(cron = "0 0 3 * * ?")
    public void deleteUsers() {
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);
        List<User> usersToDelete = userRepository.findAllByDeletedAtBefore(twoWeeksAgo);
        if (!usersToDelete.isEmpty()) {
            userRepository.deleteAll(usersToDelete);
        }
    }

    // followService 에서 follow 생성 시 user Entity 가 필요하기에 Entity 반환 타입의 메서드 생성
    @Transactional(readOnly = true)
    public User getUserEntity(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER)
        );
    }

    // 여러명의 유저들을 리스트로 반환하는 메서드
    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersByIds(List<Long> userIds) {

        List<User> Users = userRepository.findByIdIn(userIds);
        return Users.stream().map(UserResponseDto::of).toList();
    }
}
