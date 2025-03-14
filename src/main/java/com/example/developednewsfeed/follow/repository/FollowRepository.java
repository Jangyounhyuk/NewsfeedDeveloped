package com.example.developednewsfeed.follow.repository;

import com.example.developednewsfeed.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @Query("SELECT f.following.id FROM Follow f WHERE f.follower.id = :followerId")
    List<Long> findFollowingIdByFollowerId(@Param("followerId") Long followerId);

    @Query("SELECT f.follower.id FROM Follow f WHERE f.following.id = :followingId")
    List<Long> findFollowerIdByFollowingId(@Param("followingId") Long followingId);
}
