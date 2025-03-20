package com.example.developednewsfeed.post.controller;

import com.example.developednewsfeed.post.dto.response.PostResponseDto;
import com.example.developednewsfeed.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private PostService postService;

    @Test
    void Post_단건_조회() throws Exception {
        // given
        Long postId = 1L;
        given(postService.get(postId)).willReturn(new PostResponseDto(
                postId,
                1L,
                "todo",
                0,
                0,
                LocalDateTime.now(),
                LocalDateTime.now()
        ));

        // when & then
        mockMvc.perform(get("/posts/{postId}", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postId))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.content").value("todo"))
                .andExpect(jsonPath("$.numberOfLikes").value(0))
                .andExpect(jsonPath("$.numberOfComments").value(0));
    }
}