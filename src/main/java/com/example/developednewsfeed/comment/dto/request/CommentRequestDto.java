package com.example.developednewsfeed.comment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @NotNull
    private String content;
}
