package com.example.developednewsfeed.like.controller;

import com.example.developednewsfeed.common.annotation.Auth;
import com.example.developednewsfeed.common.dto.AuthUser;
import com.example.developednewsfeed.like.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    // 댓글 좋아요 생성
    @PostMapping("likes/posts/{postId}")
    public ResponseEntity<Void> insert(
            @Auth AuthUser authUser,
            @PathVariable Long postId
    ) {
        postLikeService.insert(authUser, postId);
        return ResponseEntity.ok().build();
    }

    // 댓글 좋아요 취소
    @DeleteMapping("likes/posts/{postId}")
    public ResponseEntity<Void> delete(
            @Auth AuthUser authUser,
            @PathVariable Long postId
    ) {
        postLikeService.delete(authUser, postId);
        return ResponseEntity.ok().build();
    }
}
