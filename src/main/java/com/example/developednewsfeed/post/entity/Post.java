package com.example.developednewsfeed.post.entity;

import com.example.developednewsfeed.common.entity.BaseEntity;
import com.example.developednewsfeed.post.dto.response.PostResponseDto;
import com.example.developednewsfeed.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@FilterDef(name = "activePostFilter")
@Filter(name = "activePostFilter", condition = "deleted_at is null")
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String content;
    private int numberOfLikes;
    private int numberOfComments;
    private LocalDateTime deletedAt;

    @Builder
    public Post(User user, String content, int numberOfLikes, int numberOfComments) {
        this.user = user;
        this.content = content;
        this.numberOfLikes = numberOfLikes;
        this.numberOfComments = numberOfComments;
    }

    public void update(String content) {
        this.content = content;
    }

    public void changeDeletedAt(LocalDateTime localDateTime) {
        this.deletedAt = localDateTime;
    }

    public void updateNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    public void updateNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }
}
