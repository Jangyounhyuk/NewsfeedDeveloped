package com.example.developednewsfeed.auth.controller;

import com.example.developednewsfeed.auth.dto.request.SigninRequestDto;
import com.example.developednewsfeed.auth.dto.request.SignupRequestDto;
import com.example.developednewsfeed.auth.dto.response.SigninResponseDto;
import com.example.developednewsfeed.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public void signup(@Validated @RequestBody SignupRequestDto requestDto) {
        authService.signup(requestDto);
    }

    @PostMapping("/auth/signin")
    public SigninResponseDto signin(@RequestBody SigninRequestDto requestDto) {
        return authService.signin(requestDto);
    }
}
