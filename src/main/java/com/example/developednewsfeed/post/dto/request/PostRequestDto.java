package com.example.developednewsfeed.post.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostRequestDto {

    @NotNull
    private String content;
}
