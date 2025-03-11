package com.example.developednewsfeed.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // NOT FOUND EXCEPTION
    NOT_FOUND_USER("없는 사용자 입니다.", HttpStatus.NOT_FOUND),
    ALREADY_EXIST_EMAIL("이미 사용중인 이메일입니다.", HttpStatus.BAD_REQUEST),

    // USER CLASS ERROR
    MISMATCHED_PASSWORD("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    SAME_PASSWORD("기존 비밀번호와 동일합니다.", HttpStatus.BAD_REQUEST)
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
