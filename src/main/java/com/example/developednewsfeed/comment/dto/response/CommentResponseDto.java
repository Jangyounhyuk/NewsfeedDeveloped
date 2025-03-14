package com.example.developednewsfeed.comment.dto.response;

import com.example.developednewsfeed.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private final Long id;
    private final Long postId;
    private final String content;

    public CommentResponseDto(Long id, Long postId, String content) {
        this.id = id;
        this.postId = postId;
        this.content = content;
    }

    public static CommentResponseDto of(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getPost().getId(),
                comment.getContent()
                );
    }
}
