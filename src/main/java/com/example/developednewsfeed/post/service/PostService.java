package com.example.developednewsfeed.post.service;

import com.example.developednewsfeed.common.dto.AuthUser;
import com.example.developednewsfeed.common.exception.ApplicationException;
import com.example.developednewsfeed.common.exception.ErrorCode;
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

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

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
    public Page<PostResponseDto> getAll(int page, int size) {

        // activePostFilter 필터 활성화 메서드
        postRepository.enableSoftDeleteFilter();

        // 클라이언트에서 1부터 전달된 페이지 번호를 0 기반으로 조정
        int adjustedPage = (page > 0) ? page - 1 : 0;

        // 정렬 default 는 생성일 기준 내림차순
        PageRequest pageable = PageRequest.of(adjustedPage, size, Sort.by("createdAt").descending());
        Page<Post> postPage = postRepository.findAll(pageable);
        return postPage.map(PostResponseDto::of);
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
}
