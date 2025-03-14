package com.example.developednewsfeed.follow.service;

import com.example.developednewsfeed.common.dto.AuthUser;
import com.example.developednewsfeed.common.exception.ApplicationException;
import com.example.developednewsfeed.common.exception.ErrorCode;
import com.example.developednewsfeed.follow.entity.Follow;
import com.example.developednewsfeed.follow.repository.FollowRepository;
import com.example.developednewsfeed.user.dto.response.UserResponseDto;
import com.example.developednewsfeed.user.entity.User;
import com.example.developednewsfeed.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    @Transactional
    public void follow(AuthUser authUser, Long followingId) {

        User followerUser = userService.getUserEntity(authUser.getId()); // 팔로우 신청하는 유저(본인)
        User followingUser = userService.getUserEntity(followingId); // 팔로우를 받는 유저(대상)

        // 본인에게는 팔로우 신청 불가
        if (followingUser.equals(followerUser)) {
            throw new ApplicationException(ErrorCode.CANT_FOLLOW_MYSELF);
        }
        // 중복 팔로우 신청 불가
        if (followRepository.existsByFollowerIdAndFollowingId(followerUser.getId(), followingUser.getId())) {
            throw new ApplicationException(ErrorCode.ALREADY_EXIST_FOLLOW);
        }

        Follow follow = Follow.builder()
                .follower(followerUser)
                .following(followingUser)
                .build();
        followRepository.save(follow);
    }

    @Transactional
    public void unFollow(AuthUser authUser, Long followingId) {

        User followerUser = userService.getUserEntity(authUser.getId()); // 팔로우 신청하는 유저(본인)
        User followingUser = userService.getUserEntity(followingId); // 팔로우를 받는 유저(대상)

        Follow follow = followRepository.findByFollowerIdAndFollowingId(
                followerUser.getId(),
                followingUser.getId()
        ).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOLLOWING_USER)
        );

        followRepository.delete(follow);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getFollowingList(AuthUser authUser) {

        User user = userService.getUserEntity(authUser.getId());

        List<Long> followingIds = followRepository.findFollowingIdByFollowerId(user.getId());

        // 내가 follow 하고 있는 유저가 없을 경우 예외 처리
        if (followingIds.isEmpty()) {
            throw new ApplicationException(ErrorCode.NO_FOLLOWING_USERS);
        }

        return userService.getUsersByIds(followingIds);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getFollowerList(AuthUser authUser) {

        User user = userService.getUserEntity(authUser.getId());

        List<Long> followers = followRepository.findFollowerIdByFollowingId(user.getId());

        //  나를 follow 하고 있는 유저가 없을 경우 예외 처리
        if (followers.isEmpty()) {
            throw new ApplicationException(ErrorCode.NO_FOLLOWER_USERS);
        }

        return userService.getUsersByIds(followers);
    }

    // 내가  팔로우하고 있는 유저들의 id를 가져오는 메서드
    public List<Long> getFollowingIds(Long followerId) {
        List<Long> followingIds = followRepository.findFollowingIdByFollowerId(followerId);

        // 내가 follow 하고 있는 유저가 없을 경우 예외 처리
        if (followingIds.isEmpty()) {
            throw new ApplicationException(ErrorCode.NO_FOLLOWING_USERS);
        }

        return followingIds;
    }
}
