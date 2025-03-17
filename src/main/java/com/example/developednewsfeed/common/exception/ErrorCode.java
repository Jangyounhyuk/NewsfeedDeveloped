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
    NOT_LIKED_COMMENT("좋아요를 누르지 않은 댓글입니다", HttpStatus.NOT_FOUND),
    NOT_LIKED_POST("좋아요를 누르지 않은 게시물입니다", HttpStatus.NOT_FOUND),

    // USER CLASS ERROR
    MISMATCHED_PASSWORD("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    SAME_PASSWORD("기존 비밀번호와 동일합니다.", HttpStatus.BAD_REQUEST),

    // POST CLASS ERROR
    MISMATCHED_POST_WITH_USER("로그인한 유저는 게시물 작성자와 다른 유저입니다. 권한이 없습니다.", HttpStatus.UNAUTHORIZED),

    // COMMENT CLASS ERROR
    MISMATCHED_COMMENT_WITH_POST("해당 게시물에서 작성된 댓글이 아닙니다.", HttpStatus.BAD_REQUEST),
    MISMATCHED_COMMENT_WITH_USER("해당 댓글을 작성한 유저가 아닙니다. 권한이 없습니다.", HttpStatus.UNAUTHORIZED),

    // FOLLOW CLASS ERROR
    CANT_FOLLOW_MYSELF("본인에게는 팔로우 신청을 할 수 없습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_EXIST_FOLLOW("이미 팔로우 관계입니다.", HttpStatus.BAD_REQUEST),
    NOT_FOLLOWING_USER("팔로우하지 않은 유저입니다.", HttpStatus.BAD_REQUEST),
    NO_FOLLOWING_USERS("팔로우하는 유저가 없습니다.", HttpStatus.BAD_REQUEST),
    NO_FOLLOWER_USERS("나를 팔로우하는 유저가 없습니다", HttpStatus.BAD_REQUEST),

    // LIKE CLASS ERROR
    ALREADY_LIKED_COMMENT("이미 좋아요를 누른 댓글입니다", HttpStatus.BAD_REQUEST),
    ALREADY_LIKED_POST("이미 좋아요를 누른 게시물입니다", HttpStatus.BAD_REQUEST),
    CANT_LIKE_YOUR_POST("본인의 게시물에는 좋아요를 할 수 없습니다.", HttpStatus.BAD_REQUEST),
    CANT_LIKE_YOUR_COMMENT("본인의 댓글에는 좋아요를 할 수 없습니다.", HttpStatus.BAD_REQUEST)
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
