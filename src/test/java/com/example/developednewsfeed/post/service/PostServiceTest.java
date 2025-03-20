package com.example.developednewsfeed.post.service;

import com.example.developednewsfeed.common.dto.AuthUser;
import com.example.developednewsfeed.common.exception.ApplicationException;
import com.example.developednewsfeed.common.exception.ErrorCode;
import com.example.developednewsfeed.follow.service.FollowService;
import com.example.developednewsfeed.post.dto.request.PostRequestDto;
import com.example.developednewsfeed.post.dto.response.PostResponseDto;
import com.example.developednewsfeed.post.entity.Post;
import com.example.developednewsfeed.post.repository.PostRepository;
import com.example.developednewsfeed.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private FollowService followService;
    @InjectMocks
    private PostService postService;

    @Test
    void 게시물_등록_테스트() {
        // given
        String content = "todo";

        AuthUser authUser = new AuthUser(1L, "test@example.com");
        PostRequestDto request = new PostRequestDto();
        ReflectionTestUtils.setField(request, "content", content);
        User user = User.fromAuthUser(authUser);
        Post post = Post.builder()
                .user(user)
                .content(request.getContent())
                .numberOfComments(0)
                .build();

        given(postRepository.save(any(Post.class))).willReturn(post);

        // when
        PostResponseDto result = postService.save(authUser, request);

        // then
        assertNotNull(result);
        assertEquals(content, result.getContent());
    }

    @Test
    void 게시물_다건_조회_테스트() {
        // given
        String orderBy = "createdAt";
        int page = 1;
        int size = 10;
        int adjustedPage = (page > 0) ? page - 1 : 0;

        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);

        String content1 = "todo1";
        Post post1 = new Post();
        ReflectionTestUtils.setField(post1, "content", content1);
        ReflectionTestUtils.setField(post1, "user", user);

        String content2 = "todo2";
        Post post2 = new Post();
        ReflectionTestUtils.setField(post2, "content", content2);
        ReflectionTestUtils.setField(post2, "user", user);

        PageRequest pageable = PageRequest.of(adjustedPage, size, Sort.by(orderBy).descending());
        List<Post> posts = List.of(post1, post2);
        Page<Post> postPage = new PageImpl<>(posts);

        doNothing().when(postRepository).enableSoftDeleteFilter();
        given(postRepository.findAll(pageable)).willReturn(postPage);

        // when
        Page<PostResponseDto> result = postService.getAll(orderBy, page, size);

        // then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(content1, result.getContent().get(0).getContent());
        assertEquals(content2, result.getContent().get(1).getContent());
    }

    @Test
    void 좋아요_순_정렬_조회_테스트() {
        // given
        String orderBy = "numberOfLikes";
        int page = 1;
        int size = 10;
        int adjustedPage = (page > 0) ? page - 1 : 0;

        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);

        String content1 = "todo1";
        Post post1 = new Post();
        ReflectionTestUtils.setField(post1, "content", content1);
        ReflectionTestUtils.setField(post1, "user", user);
        ReflectionTestUtils.setField(post1, "numberOfLikes", 0); // 좋아요 수 적음

        String content2 = "todo2";
        Post post2 = new Post();
        ReflectionTestUtils.setField(post2, "content", content2);
        ReflectionTestUtils.setField(post2, "user", user);
        ReflectionTestUtils.setField(post2, "numberOfLikes", 1); // 좋아요 수 많음

        Sort sort = Sort.by(Sort.Order.desc("numberOfLikes"), Sort.Order.desc("createdAt"));
        PageRequest pageable = PageRequest.of(adjustedPage, size, sort);

        List<Post> posts = List.of(post2, post1); // List.of()는 정렬되지 않은 기본 순서로 제공하기 때문에 의도적으로 순서를 바꿔야 함
        Page<Post> postPage = new PageImpl<>(posts);

        doNothing().when(postRepository).enableSoftDeleteFilter();
        given(postRepository.findAll(pageable)).willReturn(postPage);

        // when
        Page<PostResponseDto> result = postService.getAll(orderBy, page, size);

        // then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(content2, result.getContent().get(0).getContent()); // 좋아요 수 많은 게시물 우선
        assertEquals(content1, result.getContent().get(1).getContent());
    }

    @Test
    void 수정일_순_정렬_조회_테스트() {
        // given
        String orderBy = "updatedAt";
        int page = 1;
        int size = 10;
        int adjustedPage = (page > 0) ? page - 1 : 0;

        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);

        String content1 = "todo1";
        Post post1 = new Post();
        ReflectionTestUtils.setField(post1, "content", content1);
        ReflectionTestUtils.setField(post1, "user", user);
        ReflectionTestUtils.setField(post1, "updatedAt", LocalDateTime.now().minusDays(2));

        String content2 = "todo2";
        Post post2 = new Post();
        ReflectionTestUtils.setField(post2, "content", content2);
        ReflectionTestUtils.setField(post2, "user", user);
        ReflectionTestUtils.setField(post2, "updatedAt", LocalDateTime.now().minusDays(1)); // 수정일이 더 최신임

        Sort sort = Sort.by(Sort.Order.desc("updatedAt"));
        PageRequest pageable = PageRequest.of(adjustedPage, size, sort);

        List<Post> posts = List.of(post2, post1); // List.of()는 정렬되지 않은 기본 순서로 제공하기 때문에 의도적으로 순서를 바꿔야 함
        Page<Post> postPage = new PageImpl<>(posts);

        doNothing().when(postRepository).enableSoftDeleteFilter();
        given(postRepository.findAll(pageable)).willReturn(postPage);

        // when
        Page<PostResponseDto> result = postService.getAll(orderBy, page, size);

        // then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(content2, result.getContent().get(0).getContent()); // 좋아요 수 많은 게시물 우선
        assertEquals(content1, result.getContent().get(1).getContent());
    }

    @Test
    void 게시물_날짜_필터_적용_다건_조회_테스트() {
        // given
        int page = 1;
        int size = 10;
        LocalDate searchStartDate = LocalDate.now().minusDays(1);
        LocalDate searchEndDate = LocalDate.now().plusDays(1);
        LocalDateTime start = searchStartDate.atStartOfDay();
        LocalDateTime end = searchEndDate.atTime(23, 59, 59);

        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);

        String content1 = "todo1";
        Post post1 = new Post();
        ReflectionTestUtils.setField(post1, "content", content1);
        ReflectionTestUtils.setField(post1, "user", user);
        ReflectionTestUtils.setField(post1, "createdAt", LocalDateTime.now());

        String content2 = "todo2";
        Post post2 = new Post();
        ReflectionTestUtils.setField(post2, "content", content2);
        ReflectionTestUtils.setField(post2, "user", user);
        ReflectionTestUtils.setField(post2, "createdAt", LocalDateTime.now());

        int adjustedPage = (page > 0) ? page - 1 : 0;
        PageRequest pageable = PageRequest.of(adjustedPage, size, Sort.by("createdAt").descending());
        List<Post> posts = List.of(post1, post2);
        Page<Post> postPage = new PageImpl<>(posts);

        doNothing().when(postRepository).enableSoftDeleteFilter();
        given(postRepository.findSearchedPosts(pageable, start, end)).willReturn(postPage);

        // when
        Page<PostResponseDto> result = postService.getFilteringDatePosts(page, size, searchStartDate, searchEndDate);

        // then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(content1, result.getContent().get(0).getContent());
        assertEquals(content2, result.getContent().get(1).getContent());
        result.getContent().forEach(post -> {
            LocalDateTime createdAt = post.getCreatedAt();
            assertTrue(createdAt.isAfter(start) || createdAt.isEqual(start));
            assertTrue(createdAt.isBefore(end) || createdAt.isEqual(end));
        });
    }

    @Test
    void 종료일이_시작일보다_앞설_때_예외() {
        // given
        int page = 1;
        int size = 10;
        LocalDate searchStartDate = LocalDate.now().minusDays(1);
        LocalDate searchEndDate = LocalDate.now().minusDays(2);

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> postService.getFilteringDatePosts(page, size, searchStartDate, searchEndDate));
        assertEquals(ErrorCode.BAD_INPUT_DATE, exception.getErrorCode());
    }

    @Test
    void 팔로잉_사용자들의_게시물_다건_조회_테스트() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com");
        String orderBy = "createdAt";
        int page = 1;
        int size = 10;

        Long userId1 = 1L; // 게시물1 작성자
        Long userId2 = 2L; // 게시물2 작성자
        List<Long> followingIds = List.of(userId1, userId2); // 내가 팔로우 하고 있는 사용자들의 id

        User user1 = new User();
        ReflectionTestUtils.setField(user1, "id", userId1);
        User user2 = new User();
        ReflectionTestUtils.setField(user2, "id", userId2);

        String content1 = "todo1";
        Post post1 = new Post();
        ReflectionTestUtils.setField(post1, "content", content1);
        ReflectionTestUtils.setField(post1, "user", user1);

        String content2 = "todo2";
        Post post2 = new Post();
        ReflectionTestUtils.setField(post2, "content", content2);
        ReflectionTestUtils.setField(post2, "user", user2);

        int adjustedPage = (page > 0) ? page - 1 : 0;
        PageRequest pageable = PageRequest.of(adjustedPage, size, Sort.by("createdAt").descending());
        List<Post> posts = List.of(post1, post2);
        Page<Post> postPage = new PageImpl<>(posts);


        doNothing().when(postRepository).enableSoftDeleteFilter();
        given(followService.getFollowingIds(anyLong())).willReturn(followingIds);
        given(postRepository.findByUserIdIn(followingIds, pageable)).willReturn(postPage);

        // when
        Page<PostResponseDto> result = postService.getFollowingPosts(authUser, orderBy, page, size);

        // then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(content1, result.getContent().get(0).getContent());
        assertEquals(content2, result.getContent().get(1).getContent());
    }

    @Test
    void 게시물_단건_조회_테스트() {
        // given
        Long postId = 1L;

        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);

        Post post = new Post();
        ReflectionTestUtils.setField(post, "id", postId);
        ReflectionTestUtils.setField(post, "user", user);

        doNothing().when(postRepository).enableSoftDeleteFilter();
        given(postRepository.findByIdWithFilter(anyLong())).willReturn(Optional.of(post));

        // when
        PostResponseDto result = postService.get(postId);

        // then
        assertNotNull(result);
        assertEquals(postId, result.getId());
    }

    @Test
    void 게시물_단건_조회_시_postId가_없을_때_예외() {
        // given
        Long postId = 1L;

        doNothing().when(postRepository).enableSoftDeleteFilter();
        given(postRepository.findByIdWithFilter(anyLong())).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> postService.get(postId));
        assertEquals(ErrorCode.NOT_FOUND_POST, exception.getErrorCode());
    }

    @Test
    void 게시물_수정_테스트() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com");
        Long postId = 1L;
        PostRequestDto requestDto = new PostRequestDto();
        ReflectionTestUtils.setField(requestDto, "content", "updatedContent");

        User user = User.fromAuthUser(authUser);
        Post post = new Post();
        ReflectionTestUtils.setField(post, "id", postId);
        ReflectionTestUtils.setField(post, "user", user);

        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        // when
        PostResponseDto result = postService.update(authUser, postId, requestDto);

        // then
        assertNotNull(result);
        assertEquals(postId, result.getId());
        assertEquals("updatedContent", result.getContent());
    }

    @Test
    void 게시물_수정_시_postId가_없을_때_예외() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com");
        Long postId = 1L;

        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> postService.update(authUser, postId, new PostRequestDto()));
        assertEquals(ErrorCode.NOT_FOUND_POST, exception.getErrorCode());
    }

    @Test
    void 게시물_수정_시_작성자가_아닐_때_예외() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com");
        Long postId = 1L;

        User differentUser = new User();
        ReflectionTestUtils.setField(differentUser, "id", 2L);

        Post post = new Post();
        ReflectionTestUtils.setField(post, "id", postId);
        ReflectionTestUtils.setField(post, "user", differentUser);

        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> postService.update(authUser, postId, new PostRequestDto()));
        assertEquals(ErrorCode.MISMATCHED_POST_WITH_USER, exception.getErrorCode());
    }

    @Test
    void 게시물_삭제_테스트() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com");
        Long postId = 1L;

        User user = User.fromAuthUser(authUser);
        Post post = new Post();
        ReflectionTestUtils.setField(post, "id", postId);
        ReflectionTestUtils.setField(post, "user", user);

        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        // when
        postService.delete(authUser, postId);

        // then
        assertNotNull(post.getDeletedAt());
    }

    @Test
    void 게시물_삭제_시_postId가_없을_때_예외() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com");
        Long postId = 1L;

        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> postService.delete(authUser, postId));
        assertEquals(ErrorCode.NOT_FOUND_POST, exception.getErrorCode());
    }

    @Test
    void 게시물_삭제_시_작성자가_아닐_때_예외() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com");
        Long postId = 1L;

        User differentUser = new User();
        ReflectionTestUtils.setField(differentUser, "id", 2L);

        Post post = new Post();
        ReflectionTestUtils.setField(post, "id", postId);
        ReflectionTestUtils.setField(post, "user", differentUser);

        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> postService.delete(authUser, postId));
        assertEquals(ErrorCode.MISMATCHED_POST_WITH_USER, exception.getErrorCode());
        assertNull(post.getDeletedAt());
    }

    @Test
    void 게시물_복원_테스트() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com");
        Long postId = 1L;

        User user = User.fromAuthUser(authUser);
        Post post = new Post();
        ReflectionTestUtils.setField(post, "id", postId);
        ReflectionTestUtils.setField(post, "user", user);
        ReflectionTestUtils.setField(post, "deletedAt", LocalDateTime.now());

        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        // when
        postService.restore(authUser, postId);

        // then
        assertNull(post.getDeletedAt());
    }

    @Test
    void 게시물_복원_시_postId가_없을_때_예외() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com");
        Long postId = 1L;

        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> postService.restore(authUser, postId));
        assertEquals(ErrorCode.NOT_FOUND_POST, exception.getErrorCode());
    }

    @Test
    void 게시물_복원_시_작성자가_아닐_때_예외() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com");
        Long postId = 1L;

        User differentUser = new User();
        ReflectionTestUtils.setField(differentUser, "id", 2L);

        Post post = new Post();
        ReflectionTestUtils.setField(post, "id", postId);
        ReflectionTestUtils.setField(post, "user", differentUser);
        ReflectionTestUtils.setField(post, "deletedAt", LocalDateTime.now());

        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> postService.restore(authUser, postId));
        assertEquals(ErrorCode.MISMATCHED_POST_WITH_USER, exception.getErrorCode());
        assertNotNull(post.getDeletedAt());
    }

    @Test
    void 삭제요청된_게시물_2주_지난_후_물리_삭제_테스트() {
        // given
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);

        Post post1 = new Post();
        ReflectionTestUtils.setField(post1, "deletedAt", LocalDateTime.now().minusWeeks(3));
        Post post2 = new Post();
        ReflectionTestUtils.setField(post2, "deletedAt", LocalDateTime.now().minusWeeks(3));

        List<Post> postToDelete = List.of(post1, post2);

        given(postRepository.findAllByDeletedAtBefore(any(LocalDateTime.class))).willReturn(postToDelete);

        // when
        postService.deletePosts();

        // then
        verify(postRepository, times(1)).deleteAll(postToDelete);
    }

    @Test
    void postId로_Post_Entity_반환_테스트() {
        // given
        Long postId = 1L;

        Post post = new Post();
        ReflectionTestUtils.setField(post, "id", postId);

        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        // when
        Post result = postService.getPostEntity(postId);

        // then
        assertNotNull(result);
        assertEquals(postId, result.getId());
    }

    @Test
    void postId로_Post_Entity_반환시_postId가_없을_때_예외() {
        // given
        Long postId = 1L;

        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // when & then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> postService.getPostEntity(postId));
        assertEquals(ErrorCode.NOT_FOUND_POST, exception.getErrorCode());
    }
}