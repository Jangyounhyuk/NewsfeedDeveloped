package com.example.developednewsfeed.post.controller;

import com.example.developednewsfeed.common.annotation.Auth;
import com.example.developednewsfeed.common.dto.AuthUser;
import com.example.developednewsfeed.post.dto.request.PostRequestDto;
import com.example.developednewsfeed.post.dto.response.PostResponseDto;
import com.example.developednewsfeed.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> save(
            @Auth AuthUser authUser,
            @Valid @RequestBody PostRequestDto requestDto
            ) {
        return new ResponseEntity<>(postService.save(authUser, requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/posts")
    public ResponseEntity<Page<PostResponseDto>> get(
            @RequestParam(defaultValue = "createdAt") String orderBy,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(postService.getAll(orderBy, page, size));
    }

    @GetMapping("/posts/search")
    public Page<PostResponseDto> getFilteringDatePosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "19000101") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate searchStartDate,
            @RequestParam(defaultValue = "99991231") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate searchEndDate
            ) {
        return postService.getFilteringDatePosts(page, size, searchStartDate, searchEndDate);
    }

    // 팔로우 하고 있는 유저들의 게시물 조회
    @GetMapping("/posts/followingPosts")
    public ResponseEntity<Page<PostResponseDto>> get(
            @Auth AuthUser authUser,
            @RequestParam(defaultValue = "createdAt") String orderBy,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(postService.getFollowingPosts(authUser, orderBy, page, size));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> get(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.get(postId));
    }

    @PatchMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> update(
            @Auth AuthUser authUser,
            @PathVariable Long postId,
            @Valid @RequestBody PostRequestDto requestDto
            ) {
        return ResponseEntity.ok(postService.update(authUser, postId, requestDto));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> delete(
            @Auth AuthUser authUser,
            @PathVariable Long postId
    ) {
        postService.delete(authUser, postId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/posts/restore/{postId}")
    public ResponseEntity<Void> restore(
            @Auth AuthUser authUser,
            @PathVariable Long postId
    ) {
        postService.restore(authUser, postId);
        return ResponseEntity.ok().build();
    }
}
