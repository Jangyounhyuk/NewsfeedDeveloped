package com.example.developednewsfeed.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ALREADY_EXIST_EMAIL("이미 사용중인 이메일입니다.", HttpStatus.BAD_REQUEST),

    // NOT FOUND EXCEPTION
    NOT_FOUND_USER("없는 사용자 입니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_POST("없는 게시물 입니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_COMMENT("없는 댓글 입니다.", HttpStatus.NOT_FOUND),

    // USER CLASS ERROR
    MISMATCHED_PASSWORD("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    SAME_PASSWORD("기존 비밀번호와 동일합니다.", HttpStatus.BAD_REQUEST),

    // POST CLASS ERROR
    MISMATCHED_POST_WITH_USER("로그인한 유저는 게시물 작성자와 다른 유저입니다. 권한이 없습니다.", HttpStatus.UNAUTHORIZED),

    // COMMENT CLASS ERROR
    MISMATCHED_COMMENT_WITH_POST("해당 게시물에서 작성된 댓글이 아닙니다.", HttpStatus.BAD_REQUEST),
    MISMATCHED_COMMENT_WITH_USER("해당 댓글을 작성한 유저가 아닙니다. 권한이 없습니다.", HttpStatus.UNAUTHORIZED)
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
