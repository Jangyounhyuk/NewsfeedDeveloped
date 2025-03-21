package com.example.developednewsfeed.comment.dto.response;

import com.example.developednewsfeed.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private final Long id;
    private final Long userId;
    private final Long postId;
    private final String content;
    private final int numberOfLikes;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CommentResponseDto(
            Long id,
            Long userId,
            Long postId,
            String content,
            int numberOfLikes,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.content = content;
        this.numberOfLikes = numberOfLikes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CommentResponseDto of(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getUser().getId(),
                comment.getPost().getId(),
                comment.getContent(),
                comment.getNumberOfLikes(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
                );
    }
}
