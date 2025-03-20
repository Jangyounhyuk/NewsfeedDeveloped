package com.example.developednewsfeed.user.service;

import com.example.developednewsfeed.common.config.PasswordEncoder;
import com.example.developednewsfeed.common.exception.ApplicationException;
import com.example.developednewsfeed.common.exception.ErrorCode;
import com.example.developednewsfeed.user.dto.request.ChangePasswordRequestDto;
import com.example.developednewsfeed.user.dto.request.UserDeleteRequestDto;
import com.example.developednewsfeed.user.dto.request.UserRestoreRequestDto;
import com.example.developednewsfeed.user.dto.request.UserUpdateRequestDto;
import com.example.developednewsfeed.user.dto.response.UserResponseDto;
import com.example.developednewsfeed.user.entity.User;
import com.example.developednewsfeed.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    void 사용자_저장_테스트() {
        // given
        String email = "test@example.com";
        String password = "Qwer123!";
        String selfIntroduction = "hi";
        String encodedPassword = "EncodedPassword1";

        given(userRepository.existsByEmail(email)).willReturn(false);
        given(passwordEncoder.encode(password)).willReturn(encodedPassword);

        // when
        UserResponseDto result = userService.save(email, password, selfIntroduction);

        // then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(selfIntroduction, result.getSelfIntroduction());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void 사용자_저장_시_중복_이메일_예외() {
        // given
        String email = "test@example.com";
        String password = "Qwer123!";
        String selfIntroduction = "hi";

        given(userRepository.existsByEmail(email)).willReturn(true);

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.save(email, password, selfIntroduction));
        assertEquals(ErrorCode.ALREADY_EXIST_EMAIL, exception.getErrorCode());
    }

    @Test
    void 사용자_이메일로_조회_테스트() {
        // given
        String email = "test@example.com";
        User user = User.builder()
                .email(email)
                .password("encodedPassword")
                .selfIntroduction("hi")
                .build();

        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));

        // when
        UserResponseDto result = userService.findByEmail(email);

        // then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void 사용자_이메일로_조회_시_이메일_존재하지_않을_때_예외() {
        // given
        String email = "test@example.com";

        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.findByEmail(email));
        assertEquals(ErrorCode.NOT_FOUND_USER, exception.getErrorCode());
    }

    @Test
    void 사용자_userId로_조회_테스트() {
        // given
        Long userId = 1L;

        User user = new User();
        ReflectionTestUtils.setField(user, "id", userId);

        doNothing().when(userRepository).enableSoftDeleteFilter();
        given(userRepository.findByIdWithFilter(anyLong())).willReturn(Optional.of(user));

        // when
        UserResponseDto result = userService.get(userId);

        // then
        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    void 사용자_userId로_조회_시_userId가_존재하지_않을_때_예외() {
        // given
        Long userId = 1L;

        given(userRepository.findByIdWithFilter(anyLong())).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.get(userId));
        assertEquals(ErrorCode.NOT_FOUND_USER, exception.getErrorCode());
    }

    @Test
    void 사용자_정보_수정_테스트() {
        // given
        Long id = 1L;
        String updatedSelfIntroduction = "updatedSelfIntroduction";
        UserUpdateRequestDto requestDto = new UserUpdateRequestDto();
        ReflectionTestUtils.setField(requestDto, "selfIntroduction", updatedSelfIntroduction);
        User user = User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .selfIntroduction("selfIntroduction")
                .build();

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        UserResponseDto result = userService.update(id, requestDto);

        // then
        assertNotNull(result);
        assertEquals(updatedSelfIntroduction, result.getSelfIntroduction());
    }

    @Test
    void 사용자_정보_수정_시_id가_존재하지_않을_때_예외() {
        // given
        Long id = 1L;
        UserUpdateRequestDto requestDto = new UserUpdateRequestDto();

        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.update(id, requestDto));
        assertEquals(ErrorCode.NOT_FOUND_USER, exception.getErrorCode());
    }

    @Test
    void 비밀번호_수정_테스트() {
        // given
        Long id = 1L;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String encodedNewPassword = "encodedNewPassword";
        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto();
        ReflectionTestUtils.setField(requestDto, "currentPassword", currentPassword);
        ReflectionTestUtils.setField(requestDto, "newPassword", newPassword);

        User user = User.builder()
                .email("test@example.com")
                .password("encodedCurrentPassword")
                .selfIntroduction("hi")
                .build();

        given(userRepository.findById((anyLong()))).willReturn(Optional.of(user));
        given(passwordEncoder.matches(requestDto.getNewPassword(), user.getPassword())).willReturn(false);
        given(passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())).willReturn(true);
        given(passwordEncoder.encode(requestDto.getNewPassword())).willReturn(encodedNewPassword);

        // when
        userService.changePassword(id, requestDto);

        // then
        assertEquals(encodedNewPassword, user.getPassword());
    }

    @Test
    void 비밀번호_수정_시_id가_존재하지_않을_때_예외() {
        // given
        Long id = 1L;
        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto();

        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.changePassword(id, requestDto));
        assertEquals(ErrorCode.NOT_FOUND_USER, exception.getErrorCode());
    }

    @Test
    void 비밀번호_수정_시_기존_비밀번호와_동일하게_수정할_때_예외() {
        // given
        Long id = 1L;
        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto();
        ReflectionTestUtils.setField(requestDto, "newPassword", "currentPassword");
        User user = User.builder()
                .email("test@example.com")
                .password("encodedCurrentPassword")
                .selfIntroduction("hi")
                .build();

        given(userRepository.findById((anyLong()))).willReturn(Optional.of(user));
        given(passwordEncoder.matches(requestDto.getNewPassword(), user.getPassword())).willReturn(true);

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.changePassword(id, requestDto));
        assertEquals(ErrorCode.SAME_PASSWORD, exception.getErrorCode());
    }

    @Test
    void 비밀번호_수정_시_기존_비밀번호가_틀릴_때_예외() {
        // given
        Long id = 1L;
        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto();
        ReflectionTestUtils.setField(requestDto, "currentPassword", "wrongPassword");
        User user = User.builder()
                .email("test@example.com")
                .password("encodedCurrentPassword")
                .selfIntroduction("hi")
                .build();

        given(userRepository.findById((anyLong()))).willReturn(Optional.of(user));
        given(passwordEncoder.matches(requestDto.getNewPassword(), user.getPassword())).willReturn(false);
        given(passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())).willReturn(false);

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.changePassword(id, requestDto));
        assertEquals(ErrorCode.MISMATCHED_PASSWORD, exception.getErrorCode());
    }

    @Test
    void 사용자_삭제_테스트() {
        // given
        Long id = 1L;
        UserDeleteRequestDto requestDto = new UserDeleteRequestDto();
        ReflectionTestUtils.setField(requestDto, "password", "currentPassword");
        User user = User.builder()
                .email("test@example.com")
                .password("encodedCurrentPassword")
                .selfIntroduction("hi")
                .build();

        given(userRepository.findById((anyLong()))).willReturn(Optional.of(user));
        given(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).willReturn(true);

        // when
        userService.delete(id, requestDto);

        // then
        assertNotNull(user.getDeletedAt());
    }

    @Test
    void 사용자_삭제_시_id가_존재하지_않을_때_예외() {
        // given
        Long id = 1L;
        UserDeleteRequestDto requestDto = new UserDeleteRequestDto();

        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.delete(id, requestDto));
        assertEquals(ErrorCode.NOT_FOUND_USER, exception.getErrorCode());
    }

    @Test
    void 사용자_삭제_시_입력한_비밀번호가_기존_비밀번호와_다를_때_예외() {
        // given
        Long id = 1L;
        UserDeleteRequestDto requestDto = new UserDeleteRequestDto();
        ReflectionTestUtils.setField(requestDto, "password", "wrongPassword");
        User user = User.builder()
                .email("test@example.com")
                .password("encodedCurrentPassword")
                .selfIntroduction("hi")
                .build();

        given(userRepository.findById((anyLong()))).willReturn(Optional.of(user));
        given(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).willReturn(false);

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.delete(id, requestDto));
        assertEquals(ErrorCode.MISMATCHED_PASSWORD, exception.getErrorCode());
        assertNull(user.getDeletedAt());
    }

    @Test
    void 사용자_복구_테스트() {
        // given
        Long id = 1L;
        UserRestoreRequestDto requestDto = new UserRestoreRequestDto();
        ReflectionTestUtils.setField(requestDto, "password", "currentPassword");
        User user = User.builder()
                .email("test@example.com")
                .password("encodedCurrentPassword")
                .selfIntroduction("hi")
                .build();
        ReflectionTestUtils.setField(user, "deletedAt", LocalDateTime.now());

        given(userRepository.findById((anyLong()))).willReturn(Optional.of(user));
        given(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).willReturn(true);

        // when
        userService.restore(id, requestDto);

        // then
        assertNull(user.getDeletedAt());
    }

    @Test
    void 사용자_복구_시_id가_존재하지_않을_때_예외() {
        // given
        Long id = 1L;
        UserRestoreRequestDto requestDto = new UserRestoreRequestDto();

        given(userRepository.findById((anyLong()))).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.restore(id, requestDto));
        assertEquals(ErrorCode.NOT_FOUND_USER, exception.getErrorCode());
    }

    @Test
    void 사용자_복구_시_입력한_비밀번호가_기존_비밀번호와_다를_때_예외() {
        // given
        Long id = 1L;
        UserRestoreRequestDto requestDto = new UserRestoreRequestDto();
        ReflectionTestUtils.setField(requestDto, "password", "wrongPassword");
        User user = User.builder()
                .email("test@example.com")
                .password("encodedCurrentPassword")
                .selfIntroduction("hi")
                .build();
        ReflectionTestUtils.setField(user, "deletedAt", LocalDateTime.now());

        given(userRepository.findById((anyLong()))).willReturn(Optional.of(user));
        given(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).willReturn(false);

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.restore(id, requestDto));
        assertEquals(ErrorCode.MISMATCHED_PASSWORD, exception.getErrorCode());
        assertNotNull(user.getDeletedAt());
    }

    @Test
    void 삭제요청된_사용자_2주_지난_후_물리_삭제_테스트() {
        // given
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);

        User user1 = new User();
        ReflectionTestUtils.setField(user1, "deletedAt", LocalDateTime.now().minusWeeks(3));
        User user2 = new User();
        ReflectionTestUtils.setField(user2, "deletedAt", LocalDateTime.now().minusWeeks(3));

        List<User> usersToDelete = List.of(user1, user2);

        given(userRepository.findAllByDeletedAtBefore(any(LocalDateTime.class))).willReturn(usersToDelete);

        // when
        userService.deleteUsers();

        // then
        verify(userRepository, times(1)).deleteAll(usersToDelete);
    }

    @Test
    void userId로_User_Entity_반환_테스트() {
        // given
        Long userId = 1L;
        User user = new User();
        ReflectionTestUtils.setField(user, "id", userId);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        User result = userService.getUserEntity(userId);

        // then
        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    void userId로_User_Entity_반환_시_userId가_존재하지_않을_때_예외() {
        // given
        Long userId = 1L;

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.getUserEntity(userId));
        assertEquals(ErrorCode.NOT_FOUND_USER, exception.getErrorCode());
    }

    @Test
    void 여러명의_유저들을_userId로_조회_테스트() {
        // given
        Long userId1 = 1L;
        Long userId2 = 2L;
        List<Long> userIds = List.of(userId1, userId2);

        User user1 = new User();
        ReflectionTestUtils.setField(user1, "id", userId1);
        User user2 = new User();
        ReflectionTestUtils.setField(user2, "id", userId2);
        List<User> Users = List.of(user1, user2);

        given(userRepository.findByIdIn(userIds)).willReturn(Users);

        // when
        List<UserResponseDto> result = userService.getUsersByIds(userIds);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(userId1, result.get(0).getId());
        assertEquals(userId2, result.get(1).getId());
    }
}