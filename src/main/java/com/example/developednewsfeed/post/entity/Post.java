package com.example.developednewsfeed.post.entity;

import com.example.developednewsfeed.common.entity.BaseEntity;
import com.example.developednewsfeed.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    private int numberOfComments;
    private LocalDateTime deletedAt;

    @Builder
    public Post(User user, String content, int numberOfComments) {
        this.user = user;
        this.content = content;
        this.numberOfComments = numberOfComments;
    }

    public void update(String content) {
        this.content = content;
    }

    public void changeDeletedAt(LocalDateTime localDateTime) {
        this.deletedAt = localDateTime;
    }
}
