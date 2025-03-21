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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostService postService;
    @InjectMocks
    private CommentService commentService;

    @Test
    void 댓글_생성_테스트() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com");
        Long postId = 1L;
        String content = "content1";
        CommentRequestDto requestDto = new CommentRequestDto();
        ReflectionTestUtils.setField(requestDto, "content", content);

        User user = User.fromAuthUser(authUser);
        Post post = new Post();
        ReflectionTestUtils.setField(post, "id", postId);
        ReflectionTestUtils.setField(post, "user", user);
        ReflectionTestUtils.setField(post, "numberOfComments", 0); // 초기 댓글 수

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(content)
                .build();

        given(commentRepository.save(any(Comment.class))).willReturn(comment);
        given(postService.getPostEntity(anyLong())).willReturn(post);

        // when
        CommentResponseDto result = commentService.save(authUser, postId, requestDto);

        // then
        assertNotNull(result);
        assertEquals(content, result.getContent());
        assertEquals(1, post.getNumberOfComments());
    }

    @Test
    void 댓글_수정_테스트() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com");
        Long postId = 1L;
        Long commentId = 1L;
        String content = "updatedContent";

        CommentRequestDto requestDto = new CommentRequestDto();
        ReflectionTestUtils.setField(requestDto, "content", content);

        User user = User.fromAuthUser(authUser);
        Post post = new Post();
        ReflectionTestUtils.setField(post, "id", postId);
        ReflectionTestUtils.setField(post, "user", user);

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content("currentContent")
                .build();

        given(postService.getPostEntity(anyLong())).willReturn(post);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        // when
        CommentResponseDto result = commentService.update(authUser, postId, commentId, requestDto);

        // then
        assertNotNull(result);
        assertEquals(content, result.getContent());
    }

    @Test
    void 댓글_수정_시_commentId가_없을_때_예외() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com");
        Long postId = 1L;
        Long commentId = 1L;

        User user = User.fromAuthUser(authUser);
        Post post = new Post();
        ReflectionTestUtils.setField(post, "id", postId);
        ReflectionTestUtils.setField(post, "user", user);

        given(postService.getPostEntity(anyLong())).willReturn(post);
        given(commentRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> commentService.update(authUser, postId, commentId, new CommentRequestDto()));
        assertEquals(ErrorCode.NOT_FOUND_COMMENT, exception.getErrorCode());
    }

    @Test
    void 댓글_수정_시_댓글이_해당_게시물에서_작성된_댓글이_아닐_때_예외() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com");
        Long postId = 1L;
        Long commentId = 1L;
        String content = "updatedContent";

        CommentRequestDto requestDto = new CommentRequestDto();
        ReflectionTestUtils.setField(requestDto, "content", content);

        User user = User.fromAuthUser(authUser);

        Post post = new Post();
        ReflectionTestUtils.setField(post, "id", postId);
        ReflectionTestUtils.setField(post, "user", user);

        Post differentPost = new Post();
        ReflectionTestUtils.setField(differentPost, "id", 2L);
        ReflectionTestUtils.setField(differentPost, "user", user);

        Comment comment = Comment.builder()
                .user(user)
                .post(differentPost)
                .content("currentContent")
                .build();

        given(postService.getPostEntity(anyLong())).willReturn(post);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> commentService.update(authUser, postId, commentId, new CommentRequestDto()));
        assertEquals(ErrorCode.MISMATCHED_COMMENT_WITH_POST, exception.getErrorCode());
    }

    @Test
    void 댓글_수정_시_댓글_작성자가_아닐_때_예외() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com");
        Long postId = 1L;
        Long commentId = 1L;
        String content = "updatedContent";

        CommentRequestDto requestDto = new CommentRequestDto();
        ReflectionTestUtils.setField(requestDto, "content", content);

        User user = User.fromAuthUser(authUser);
        User differentUser = new User();
        ReflectionTestUtils.setField(differentUser, "id", 2L);

        Post post = new Post();
        ReflectionTestUtils.setField(post, "id", postId);
        ReflectionTestUtils.setField(post, "user", user);

        Comment comment = Comment.builder()
                .user(differentUser)
                .post(post)
                .content("currentContent")
                .build();

        given(postService.getPostEntity(anyLong())).willReturn(post);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> commentService.update(authUser, postId, commentId, new CommentRequestDto()));
        assertEquals(ErrorCode.MISMATCHED_COMMENT_WITH_USER, exception.getErrorCode());
    }

    @Test
    void 댓글_삭제_테스트() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com");
        Long postId = 1L;
        Long commentId = 1L;
        String content = "updatedContent";

        CommentRequestDto requestDto = new CommentRequestDto();
        ReflectionTestUtils.setField(requestDto, "content", content);

        User user = User.fromAuthUser(authUser);

        Post post = new Post();
        ReflectionTestUtils.setField(post, "id", postId);
        ReflectionTestUtils.setField(post, "user", user);
        ReflectionTestUtils.setField(post, "numberOfComments", 1); // 초기 댓글 수

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content("currentContent")
                .build();

        given(postService.getPostEntity(anyLong())).willReturn(post);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));
        doNothing().when(commentRepository).deleteById(anyLong());

        // when
        commentService.delete(authUser, postId, commentId);

        // then
        verify(commentRepository, times(1)).deleteById(anyLong());
        assertEquals(0, post.getNumberOfComments());
    }

    @Test
    void 댓글_삭제_시_commentId가_없을_때_예외() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com");
        Long postId = 1L;
        Long commentId = 1L;

        User user = User.fromAuthUser(authUser);
        Post post = new Post();
        ReflectionTestUtils.setField(post, "id", postId);
        ReflectionTestUtils.setField(post, "user", user);

        given(postService.getPostEntity(anyLong())).willReturn(post);
        given(commentRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> commentService.delete(authUser, postId, commentId));
        assertEquals(ErrorCode.NOT_FOUND_COMMENT, exception.getErrorCode());
    }

    @Test
    void 댓글_삭제_시_댓글이_해당_게시물에서_작성된_댓글이_아닐_때_예외() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com");
        Long postId = 1L;
        Long commentId = 1L;

        User user = User.fromAuthUser(authUser);

        Post post = new Post();
        ReflectionTestUtils.setField(post, "id", postId);
        ReflectionTestUtils.setField(post, "user", user);

        Post differentPost = new Post();
        ReflectionTestUtils.setField(differentPost, "id", 2L);
        ReflectionTestUtils.setField(differentPost, "user", user);

        Comment comment = Comment.builder()
                .user(user)
                .post(differentPost)
                .content("content")
                .build();

        given(postService.getPostEntity(anyLong())).willReturn(post);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> commentService.delete(authUser, postId, commentId));
        assertEquals(ErrorCode.MISMATCHED_COMMENT_WITH_POST, exception.getErrorCode());
    }

    @Test
    void 댓글_삭제_시_댓글_작성자가_아닐_때_예외() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com");
        Long postId = 1L;
        Long commentId = 1L;

        User user = User.fromAuthUser(authUser);
        User differentUser = new User();
        ReflectionTestUtils.setField(differentUser, "id", 2L);

        Post post = new Post();
        ReflectionTestUtils.setField(post, "id", postId);
        ReflectionTestUtils.setField(post, "user", user);

        Comment comment = Comment.builder()
                .user(differentUser)
                .post(post)
                .content("content")
                .build();

        given(postService.getPostEntity(anyLong())).willReturn(post);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> commentService.delete(authUser, postId, commentId));
        assertEquals(ErrorCode.MISMATCHED_COMMENT_WITH_USER, exception.getErrorCode());
    }

    @Test
    void commentId로_Comment_Entity_반환_테스트() {
        // given
        Long commentId = 1L;

        Comment comment = new Comment();
        ReflectionTestUtils.setField(comment, "id", commentId);

        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        // when
        Comment result = commentService.getCommentEntity(commentId);

        // then
        assertNotNull(result);
        assertEquals(commentId, result.getId());
    }

    @Test
    void Comment_Entity_반환_시_commentId가_없을_때_예외() {
        // given
        Long commentId = 1L;

        given(commentRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> commentService.getCommentEntity(commentId));
        assertEquals(ErrorCode.NOT_FOUND_COMMENT, exception.getErrorCode());
    }
}