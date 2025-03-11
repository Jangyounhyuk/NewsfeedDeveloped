package com.example.developednewsfeed.auth.service;

import com.example.developednewsfeed.auth.dto.request.SigninRequestDto;
import com.example.developednewsfeed.auth.dto.request.SignupRequestDto;
import com.example.developednewsfeed.auth.dto.response.SigninResponseDto;
import com.example.developednewsfeed.common.config.JwtUtil;
import com.example.developednewsfeed.user.dto.response.UserResponseDto;
import com.example.developednewsfeed.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Transactional
    public void signup(SignupRequestDto requestDto) {
        UserResponseDto savedUser = userService.save(
                requestDto.getEmail(),
                requestDto.getPassword(),
                requestDto.getSelfIntroduction()
        );
    }

    @Transactional(readOnly = true)
    public SigninResponseDto signin(SigninRequestDto requestDto) {

        UserResponseDto savedUser = userService.findByEmail(requestDto.getEmail());

        String bearerJwt = jwtUtil.createToken(savedUser.getId(), savedUser.getEmail());
        return new SigninResponseDto(bearerJwt);
    }
}
