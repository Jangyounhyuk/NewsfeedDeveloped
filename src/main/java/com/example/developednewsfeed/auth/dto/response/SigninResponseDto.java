package com.example.developednewsfeed.auth.dto.response;

import lombok.Getter;

@Getter
public class SigninResponseDto {

    private final String bearerJwt;

    public SigninResponseDto(String bearerJwt) {
        this.bearerJwt = bearerJwt;
    }
}
