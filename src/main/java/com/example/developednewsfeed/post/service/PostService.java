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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final FollowService followService;

    @Transactional
    public PostResponseDto save(AuthUser authUser, @Valid PostRequestDto requestDto) {

        User user = User.fromAuthUser(authUser);

        Post post = Post.builder()
                .user(user)
                .content(requestDto.getContent())
                .numberOfComments(0) // 최초 댓글 수 = 0
                .build();

        postRepository.save(post);

        return PostResponseDto.of(post);
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDto> getAll(String orderBy, int page, int size) {

        // activePostFilter 필터 활성화 메서드
        postRepository.enableSoftDeleteFilter();

        // 클라이언트에서 1부터 전달된 페이지 번호를 0 기반으로 조정
        int adjustedPage = (page > 0) ? page - 1 : 0;

        // 정렬 조건 동적 생성
        Sort sort = switch (orderBy) {
            case "updatedAt" -> Sort.by("updatedAt").descending();
            case "numberOfLikes" ->
                // 좋아요 수가 동일한 경우 default 값인 생성일자 기준 내림차순 정렬 필요
                    Sort.by(Sort.Order.desc("numberOfLikes"), Sort.Order.desc("createdAt"));
            default -> Sort.by("createdAt").descending();
        };
        // 정렬 default 는 생성일 기준 내림차순
        PageRequest pageable = PageRequest.of(adjustedPage, size, sort);
        Page<Post> postPage = postRepository.findAll(pageable);
        return postPage.map(PostResponseDto::of);
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDto> getFilteringDatePosts(int page, int size, LocalDate searchStartDate, LocalDate searchEndDate) {

        // 종료일이 시작일보다 앞설 때 예외처리
        if (searchStartDate.isAfter(searchEndDate)) {
            throw new ApplicationException(ErrorCode.BAD_INPUT_DATE);
        }

        // activePostFilter 필터 활성화 메서드
        postRepository.enableSoftDeleteFilter();

        // 클라이언트에서 1부터 전달된 페이지 번호를 0 기반으로 조정
        int adjustedPage = (page > 0) ? page - 1 : 0;

        // 입력받은 LocalDate -> LocalDateTime 으로 변환
        LocalDateTime start = searchStartDate.atStartOfDay();
        LocalDateTime end = searchEndDate.atTime(23, 59, 59);

        // 정렬 default 는 생성일 기준 내림차순
        PageRequest pageable = PageRequest.of(adjustedPage, size, Sort.by("createdAt").descending());
        Page<Post> searchedPosts = postRepository.findSearchedPosts(pageable, start, end);
        return searchedPosts.map(PostResponseDto::of);
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDto> getFollowingPosts(AuthUser authUser, String orderBy, int page, int size) {

        User user = User.fromAuthUser(authUser);
        List<Long> followingIds = followService.getFollowingIds(user.getId());

        // activePostFilter 필터 활성화 메서드
        postRepository.enableSoftDeleteFilter();

        // 클라이언트에서 1부터 전달된 페이지 번호를 0 기반으로 조정
        int adjustedPage = (page > 0) ? page - 1 : 0;

        // 정렬 조건 동적 생성
        Sort sort = switch (orderBy) {
            case "updatedAt" -> Sort.by("updatedAt").descending();
            case "numberOfLikes" ->
                // 좋아요 수가 동일한 경우 default 값인 생성일자 기준 내림차순 정렬 필요
                    Sort.by(Sort.Order.desc("numberOfLikes"), Sort.Order.desc("createdAt"));
            default -> Sort.by("createdAt").descending();
        };

        // 정렬 default 는 생성일 기준 내림차순
        PageRequest pageable = PageRequest.of(adjustedPage, size, sort);
        Page<Post> followingPosts = postRepository.findByUserIdIn(followingIds, pageable);
        return followingPosts.map(PostResponseDto::of);
    }

    @Transactional(readOnly = true)
    public PostResponseDto get(Long postId) {

        // activePostFilter 필터 활성화 메서드
        postRepository.enableSoftDeleteFilter();

        // soft-delete 필터를 적용하려면 findById 메서드 사용 불가
        Post post = postRepository.findByIdWithFilter(postId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_POST)
        );

        return PostResponseDto.of(post);
    }

    @Transactional
    public PostResponseDto update(AuthUser authUser, Long postId, @Valid PostRequestDto requestDto) {

        User user = User.fromAuthUser(authUser);
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_POST)
        );
        // 게시물 작성자 본인만 게시물 수정 가능
        if (!user.getId().equals(post.getUser().getId())) {
            throw new ApplicationException(ErrorCode.MISMATCHED_POST_WITH_USER);
        }

        post.update(requestDto.getContent());
        return PostResponseDto.of(post);
    }

    @Transactional
    public void delete(AuthUser authUser, Long postId) {

        User user = User.fromAuthUser(authUser);
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_POST)
        );
        // 게시물 작성자 본인만 게시물 삭제 가능
        if (!user.getId().equals(post.getUser().getId())) {
            throw new ApplicationException(ErrorCode.MISMATCHED_POST_WITH_USER);
        }

        post.changeDeletedAt(LocalDateTime.now());
    }

    @Transactional
    public void restore(AuthUser authUser, Long postId) {

        User user = User.fromAuthUser(authUser);
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_POST)
        );
        // 게시물 작성자 본인만 삭제된 게시물 복원 가능
        if (!user.getId().equals(post.getUser().getId())) {
            throw new ApplicationException(ErrorCode.MISMATCHED_POST_WITH_USER);
        }

        post.changeDeletedAt(null);
    }

    // 삭제된 게시물 2주 지난 후 물리 삭제
    @Transactional
    @Scheduled(cron = "0 0 3 * * ?")
    public void deletePosts() {
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);
        List<Post> postToDelete = postRepository.findAllByDeletedAtBefore(twoWeeksAgo);
        if (!postToDelete.isEmpty()) {
            postRepository.deleteAll(postToDelete);
        }
    }

    // commentService, postLikeService 에서 comment 생성 시 post Entity 가 필요하기에 Entity 반환 타입의 메서드 생성
    @Transactional
    public Post getPostEntity(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_POST)
        );
    }
}
