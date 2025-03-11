package com.example.developednewsfeed.user.dto.response;

import com.example.developednewsfeed.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {

    private final Long id;
    private final String email;
    private final String selfIntroduction;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public UserResponseDto(Long id, String email, String selfIntroduction, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.selfIntroduction = selfIntroduction;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserResponseDto of(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getSelfIntroduction(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
