package com.example.developednewsfeed.user.dto.request;

import lombok.Getter;

@Getter
public class ChangePasswordRequestDto {

    private String currentPassword;
    private String newPassword;
}
