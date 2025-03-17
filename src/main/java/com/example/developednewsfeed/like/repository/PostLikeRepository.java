package com.example.developednewsfeed.like.repository;

import com.example.developednewsfeed.like.entity.PostLike;
import com.example.developednewsfeed.post.entity.Post;
import com.example.developednewsfeed.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByUserAndPost(User user, Post post);

    Optional<PostLike> findByUserAndPost(User user, Post post);
}
