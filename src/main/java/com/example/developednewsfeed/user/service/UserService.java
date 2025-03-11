package com.example.developednewsfeed.user.service;

import com.example.developednewsfeed.common.config.PasswordEncoder;
import com.example.developednewsfeed.common.exception.ApplicationException;
import com.example.developednewsfeed.common.exception.ErrorCode;
import com.example.developednewsfeed.user.dto.response.UserResponseDto;
import com.example.developednewsfeed.user.entity.User;
import com.example.developednewsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto save(String email, String password, String selfIntroduction) {

        if(userRepository.existsByEmail(email)) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_USER);
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(email, encodedPassword, selfIntroduction);
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
}
