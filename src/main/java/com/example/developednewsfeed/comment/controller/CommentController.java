package com.example.developednewsfeed.comment.controller;

import com.example.developednewsfeed.comment.dto.request.CommentRequestDto;
import com.example.developednewsfeed.comment.dto.response.CommentResponseDto;
import com.example.developednewsfeed.comment.service.CommentService;
import com.example.developednewsfeed.common.annotation.Auth;
import com.example.developednewsfeed.common.dto.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<CommentResponseDto> save(
            @Auth AuthUser authUser,
            @RequestParam Long postId,
            @Valid @RequestBody CommentRequestDto requestDto
            ) {
        return new ResponseEntity<>(commentService.save(authUser, postId, requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponseDto>> get(
            @RequestParam Long postId
    ) {
        return ResponseEntity.ok(commentService.getAll(postId));
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> update(
            @Auth AuthUser authUser,
            @RequestParam Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDto requestDto
    ) {
        return ResponseEntity.ok(commentService.update(authUser, postId, commentId, requestDto));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> delete(
            @Auth AuthUser authUser,
            @RequestParam Long postId,
            @PathVariable Long commentId
    ) {
        commentService.delete(authUser, postId, commentId);
        return ResponseEntity.ok().build();
    }
}
