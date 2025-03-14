package com.example.developednewsfeed.comment.dto.response;

import com.example.developednewsfeed.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private final Long id;
    private final Long postId;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CommentResponseDto(
            Long id,
            Long postId,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CommentResponseDto of(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getPost().getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
                );
    }
}
