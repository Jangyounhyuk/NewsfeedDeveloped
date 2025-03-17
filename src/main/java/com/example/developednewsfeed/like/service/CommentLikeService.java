package com.example.developednewsfeed.like.service;

import com.example.developednewsfeed.comment.entity.Comment;
import com.example.developednewsfeed.comment.service.CommentService;
import com.example.developednewsfeed.common.dto.AuthUser;
import com.example.developednewsfeed.common.exception.ApplicationException;
import com.example.developednewsfeed.common.exception.ErrorCode;
import com.example.developednewsfeed.like.entity.CommentLike;
import com.example.developednewsfeed.like.repository.CommentLikeRepository;
import com.example.developednewsfeed.user.entity.User;
import com.example.developednewsfeed.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final UserService userService;
    private final CommentService commentService;

    @Transactional
    public void insert(AuthUser authUser, Long commentId) {

        User user = userService.getUserEntity(authUser.getId());
        Comment comment = commentService.getCommentEntity(commentId);

        // 작성자 본인은 본인의 댓글에 좋아요 불가
        if (user.getId().equals(comment.getUser().getId())) {
            throw new ApplicationException(ErrorCode.CANT_LIKE_YOUR_COMMENT);
        }

        // 이미 좋아요 되어 있는 댓글일 경우 예외 처리
        if (commentLikeRepository.existsByUserAndComment(user, comment)) {
            throw new ApplicationException(ErrorCode.ALREADY_LIKED_COMMENT);
        }

        CommentLike newCommentLike = CommentLike.builder()
                .user(user)
                .comment(comment)
                .build();

        commentLikeRepository.save(newCommentLike);
        comment.updateNumberOfLikes(comment.getNumberOfLikes() + 1);
    }

    @Transactional
    public void delete(AuthUser authUser, Long commentId) {

        User user = userService.getUserEntity(authUser.getId());
        Comment comment = commentService.getCommentEntity(commentId);

        // 좋아요를 누른 이력이 없는 댓글일 경우 예외처리
        if (!commentLikeRepository.existsByUserAndComment(user, comment)) {
            throw new ApplicationException(ErrorCode.NOT_LIKED_COMMENT);
        }

        CommentLike commentLike = commentLikeRepository.findByUserAndComment(user, comment).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_LIKED_COMMENT)
        );
        commentLikeRepository.delete(commentLike);
        comment.updateNumberOfLikes(comment.getNumberOfLikes() - 1);
    }
}
