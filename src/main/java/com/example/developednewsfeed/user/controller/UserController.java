package com.example.developednewsfeed.user.controller;

import com.example.developednewsfeed.common.annotation.Auth;
import com.example.developednewsfeed.common.dto.AuthUser;
import com.example.developednewsfeed.user.dto.request.ChangePasswordRequestDto;
import com.example.developednewsfeed.user.dto.request.UserDeleteRequestDto;
import com.example.developednewsfeed.user.dto.request.UserRestoreRequestDto;
import com.example.developednewsfeed.user.dto.request.UserUpdateRequestDto;
import com.example.developednewsfeed.user.dto.response.UserResponseDto;
import com.example.developednewsfeed.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponseDto> get(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.get(userId));
    }

    @PatchMapping("/users")
    public ResponseEntity<UserResponseDto> update(
            @Auth AuthUser authUser,
            @RequestBody UserUpdateRequestDto requestDto
            ) {
        return ResponseEntity.ok(userService.update(authUser.getId(), requestDto));
    }

    @PutMapping("/users")
    public ResponseEntity<Void> changePassword(
            @Auth AuthUser authUser,
            @Valid @RequestBody ChangePasswordRequestDto requestDto
            ) {
        userService.changePassword(authUser.getId(), requestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users")
    public ResponseEntity<Void> delete(
            @Auth AuthUser authUser,
            @RequestBody UserDeleteRequestDto requestDto
            ) {
        userService.delete(authUser.getId(), requestDto);
        return ResponseEntity.ok().build();
    }

    // 삭제된 사용자 복구
    @PutMapping("/users/restore")
    public ResponseEntity<Void> restore(
            @Auth AuthUser authUser,
            @RequestBody UserRestoreRequestDto requestDto
            ) {
        userService.restore(authUser.getId(), requestDto);
        return ResponseEntity.ok().build();
    }
}
