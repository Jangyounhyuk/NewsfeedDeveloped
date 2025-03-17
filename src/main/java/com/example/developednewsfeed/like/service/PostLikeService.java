package com.example.developednewsfeed.like.service;

import com.example.developednewsfeed.common.dto.AuthUser;
import com.example.developednewsfeed.common.exception.ApplicationException;
import com.example.developednewsfeed.common.exception.ErrorCode;
import com.example.developednewsfeed.like.entity.PostLike;
import com.example.developednewsfeed.like.repository.PostLikeRepository;
import com.example.developednewsfeed.post.entity.Post;
import com.example.developednewsfeed.post.service.PostService;
import com.example.developednewsfeed.user.entity.User;
import com.example.developednewsfeed.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final UserService userService;
    private final PostService postService;

    @Transactional
    public void insert(AuthUser authUser, Long postId) {

        User user = userService.getUserEntity(authUser.getId());
        Post post = postService.getPostEntity(postId);

        // 작성자 본인은 본인의 게시물에 좋아요 불가
        if (user.getId().equals(post.getUser().getId())) {
            throw new ApplicationException(ErrorCode.CANT_LIKE_YOUR_POST);
        }

        // 이미 좋아요 되어 있는 댓글일 경우 예외 처리
        if (postLikeRepository.existsByUserAndPost(user, post)) {
            throw new ApplicationException(ErrorCode.ALREADY_LIKED_POST);
        }

        PostLike newPostLike = PostLike.builder()
                .user(user)
                .post(post)
                .build();

        postLikeRepository.save(newPostLike);
        post.updateNumberOfLikes(post.getNumberOfComments() + 1);
    }

    @Transactional
    public void delete(AuthUser authUser, Long postId) {

        User user = userService.getUserEntity(authUser.getId());
        Post post = postService.getPostEntity(postId);

        // 좋아요를 누른 이력이 없는 댓글일 경우 예외처리
        if (!postLikeRepository.existsByUserAndPost(user, post)) {
            throw new ApplicationException(ErrorCode.NOT_LIKED_POST);
        }

        PostLike postLike = postLikeRepository.findByUserAndPost(user, post).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_LIKED_POST)
        );
        postLikeRepository.delete(postLike);
        post.updateNumberOfLikes(post.getNumberOfLikes() - 1);
    }
}
