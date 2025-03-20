package com.example.developednewsfeed.auth.controller;

import com.example.developednewsfeed.auth.dto.request.SignupRequestDto;
import com.example.developednewsfeed.auth.service.AuthService;
import com.example.developednewsfeed.common.config.JwtUtil;
import com.example.developednewsfeed.common.config.PasswordEncoder;
import com.example.developednewsfeed.user.entity.User;
import com.example.developednewsfeed.user.repository.UserRepository;
import com.example.developednewsfeed.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;
    @MockitoBean
    private UserService userService;

    @Test
    void 회원가입() throws Exception {
        // given
        SignupRequestDto requestDto = new SignupRequestDto();
        ReflectionTestUtils.setField(requestDto, "email", "test@naver.com");
        ReflectionTestUtils.setField(requestDto, "password", "Qwer123!");
        ReflectionTestUtils.setField(requestDto, "selfIntroduction", "test");

        // when
        doNothing().when(authService).signup(any(SignupRequestDto.class));

        // then
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        verify(authService, times(1)).signup(any(SignupRequestDto.class));
    }
}