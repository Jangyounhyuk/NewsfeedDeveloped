package com.example.developednewsfeed.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotNull(message = "이메일을 입력해주세요.")
    private String email;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*()-+=]).{8,}$", message = "비밀번호는 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함하며 8자 이상이어야 합니다.")
    @NotNull(message = "비밀번호를 입력해주세요.")
    private String password;
    private String selfIntroduction;
}
