package com.example.developednewsfeed.post.repository;

import com.example.developednewsfeed.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Query("SELECT p FROM Post p WHERE p.id = :id")
    Optional<Post> findByIdWithFilter(@Param("id") Long id);

    List<Post> findAllByDeletedAtBefore(LocalDateTime twoWeeksAgo);

    Page<Post> findByUserIdIn(List<Long> followingIds, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.createdAt BETWEEN :start AND :end")
    Page<Post> findSearchedPosts(
            Pageable pageable,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
