package com.example.developednewsfeed.follow.repository;

import com.example.developednewsfeed.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    List<Long> findFollowingIdByFollowerId(Long FollowerId);

    List<Long> findFollowerIdByFollowingId(Long FollowingId);
}
