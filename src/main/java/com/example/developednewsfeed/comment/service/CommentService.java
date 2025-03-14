package com.example.developednewsfeed.comment.service;

import com.example.developednewsfeed.comment.dto.request.CommentRequestDto;
import com.example.developednewsfeed.comment.dto.response.CommentResponseDto;
import com.example.developednewsfeed.comment.entity.Comment;
import com.example.developednewsfeed.comment.repository.CommentRepository;
import com.example.developednewsfeed.common.dto.AuthUser;
import com.example.developednewsfeed.common.exception.ApplicationException;
import com.example.developednewsfeed.common.exception.ErrorCode;
import com.example.developednewsfeed.post.entity.Post;
import com.example.developednewsfeed.post.service.PostService;
import com.example.developednewsfeed.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    @Transactional
    public CommentResponseDto save(AuthUser authUser, Long postId, CommentRequestDto requestDto) {

        User user = User.fromAuthUser(authUser);
        Post post = postService.getPostEntity(postId);

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(requestDto.getContent())
                .build();

        commentRepository.save(comment);
        // 해당 게시물의 댓글 수 + 1
        post.updateNumberOfComments(post.getNumberOfComments() + 1);

        return CommentResponseDto.of(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getAll(Long postId) {

        List<Comment> comments = commentRepository.findAllByPostId(postId);
        return comments.stream().map(CommentResponseDto::of).toList();
    }

    @Transactional
    public CommentResponseDto update(AuthUser authUser, Long postId, Long commentId, CommentRequestDto requestDto) {

        User user = User.fromAuthUser(authUser);
        Post post = postService.getPostEntity(postId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_COMMENT)
        );

        // 수정하려는 댓글이 해당 게시물에서 작성된 댓글이 아닌 경우
        if (!post.getId().equals(comment.getPost().getId())) {
            throw new ApplicationException(ErrorCode.MISMATCHED_COMMENT_WITH_POST);
        }

        // 댓글 작성자 본인만 댓글 수정 가능
        if (!user.getId().equals(comment.getUser().getId())) {
            throw new ApplicationException(ErrorCode.MISMATCHED_COMMENT_WITH_USER);
        }

        comment.update(requestDto.getContent());
        return CommentResponseDto.of(comment);
    }

    @Transactional
    public void delete(AuthUser authUser, Long postId, Long commentId) {

        User user = User.fromAuthUser(authUser);
        Post post = postService.getPostEntity(postId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_COMMENT)
        );

        // 삭제하려는 댓글이 해당 게시물에서 작성된 댓글이 아닌 경우
        if (!post.getId().equals(comment.getPost().getId())) {
            throw new ApplicationException(ErrorCode.MISMATCHED_COMMENT_WITH_POST);
        }

        // 댓글 작성자 본인만 댓글 삭제 가능
        if (!user.getId().equals(comment.getUser().getId())) {
            throw new ApplicationException(ErrorCode.MISMATCHED_COMMENT_WITH_USER);
        }

        // 게시물의 numberOfComments - 1
        post.updateNumberOfComments(post.getNumberOfComments() - 1);
        commentRepository.deleteById(commentId);
    }
}
