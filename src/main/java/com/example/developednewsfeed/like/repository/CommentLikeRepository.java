package com.example.developednewsfeed.like.repository;

import com.example.developednewsfeed.comment.entity.Comment;
import com.example.developednewsfeed.like.entity.CommentLike;
import com.example.developednewsfeed.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    boolean existsByUserAndComment(User user, Comment comment);

    Optional<CommentLike> findByUserAndComment(User user, Comment comment);
}
