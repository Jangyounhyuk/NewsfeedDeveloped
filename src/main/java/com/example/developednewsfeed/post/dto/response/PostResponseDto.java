package com.example.developednewsfeed.post.dto.response;

import com.example.developednewsfeed.post.entity.Post;
import lombok.Getter;

@Getter
public class PostResponseDto {

    private final Long id;
    private final String content;
    private final int numberOfComments;

    public PostResponseDto(Long id, String content, int numberOfComments) {
        this.id = id;
        this.content = content;
        this.numberOfComments = numberOfComments;
    }

    public static PostResponseDto of(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getContent(),
                post.getNumberOfComments()
        );
    }
}
