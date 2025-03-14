package com.example.developednewsfeed.follow.controller;

import com.example.developednewsfeed.common.annotation.Auth;
import com.example.developednewsfeed.common.dto.AuthUser;
import com.example.developednewsfeed.follow.service.FollowService;
import com.example.developednewsfeed.user.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    // 팔로우 맺기
    @PostMapping("/users/follows")
    public ResponseEntity<Void> follow(
            @Auth AuthUser authUser,
            @RequestParam Long followingId
            ) {
        followService.follow(authUser, followingId);
        return ResponseEntity.ok().build();
    }

    // 팔로잉 리스트 조회
    @GetMapping("/users/follows/followings")
    public ResponseEntity<List<UserResponseDto>> getFollowingList(
            @Auth AuthUser authUser
    ) {
        return ResponseEntity.ok(followService.getFollowingList(authUser));
    }

    // 팔로워 리스트 조회
    @GetMapping("/users/follows/followers")
    public ResponseEntity<List<UserResponseDto>> getFollowerList(
            @Auth AuthUser authUser
    ) {
        return ResponseEntity.ok(followService.getFollowerList(authUser));
    }

    // 팔로우 끊기
    @DeleteMapping("/users/follows")
    public ResponseEntity<Void> unFollow(
            @Auth AuthUser authUser,
            @RequestParam Long followingId
    ) {
        followService.unFollow(authUser, followingId);
        return ResponseEntity.ok().build();
    }
}
