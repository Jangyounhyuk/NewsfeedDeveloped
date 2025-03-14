package com.example.developednewsfeed.post.dto.response;

import com.example.developednewsfeed.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private final Long id;
    private final Long userId;
    private final String content;
    private final int numberOfComments;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public PostResponseDto(
            Long id, Long userId,
            String content,
            int numberOfComments,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.numberOfComments = numberOfComments;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PostResponseDto of(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getUser().getId(),
                post.getContent(),
                post.getNumberOfComments(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
