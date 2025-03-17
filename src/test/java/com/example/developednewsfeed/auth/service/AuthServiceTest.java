package com.example.developednewsfeed.auth.service;

import com.example.developednewsfeed.auth.dto.request.SigninRequestDto;
import com.example.developednewsfeed.auth.dto.request.SignupRequestDto;
import com.example.developednewsfeed.auth.dto.response.SigninResponseDto;
import com.example.developednewsfeed.common.config.JwtUtil;
import com.example.developednewsfeed.user.dto.response.UserResponseDto;
import com.example.developednewsfeed.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private AuthService authService;

    @Test
    void 회원가입_성공() {
        // given
        SignupRequestDto requestDto = new SignupRequestDto();
        ReflectionTestUtils.setField(requestDto, "email", "abcd@naver.com");
        ReflectionTestUtils.setField(requestDto, "password", "Qwer123!");
        ReflectionTestUtils.setField(requestDto, "selfIntroduction", "hi, hello");

        UserResponseDto responseDto = new UserResponseDto(
                1L,
                "abc@naver.com",
                "hi",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        given(userService.save(requestDto.getEmail(), requestDto.getPassword(), requestDto.getSelfIntroduction()))
                .willReturn(responseDto);

        // when
        authService.signup(requestDto);

        // then
        verify(userService, times(1))
                .save(requestDto.getEmail(), requestDto.getPassword(), requestDto.getSelfIntroduction());
    }

    @Test
    void signin_성공() {
        // given
        SigninRequestDto requestDto = new SigninRequestDto();
        ReflectionTestUtils.setField(requestDto, "email", "abcd@naver.com");
        ReflectionTestUtils.setField(requestDto, "password", "Qwer123!");
        UserResponseDto responseDto = new UserResponseDto(
                1L,
                "abc@naver.com",
                "hi",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        String mockToken = "Bearer Jwt token";
        given(userService.findByEmail(requestDto.getEmail())).willReturn(responseDto);
        given(jwtUtil.createToken(responseDto.getId(), responseDto.getEmail())).willReturn(mockToken);

        // when
        SigninResponseDto result = authService.signin(requestDto);

        // then
        verify(userService, times(1)).findByEmail(requestDto.getEmail());
        verify(jwtUtil, times(1)).createToken(responseDto.getId(), responseDto.getEmail());
        assertNotNull(result);
        assertEquals(mockToken, result.getBearerJwt());
    }
}