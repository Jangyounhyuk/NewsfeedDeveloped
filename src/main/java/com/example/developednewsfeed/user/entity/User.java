package com.example.developednewsfeed.user.entity;

import com.example.developednewsfeed.common.dto.AuthUser;
import com.example.developednewsfeed.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "\"user\"")
@FilterDef(name = "activeUserFilter")
@Filter(name = "activeUserFilter", condition = "deleted_at is null")
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private String selfIntroduction;
    private LocalDateTime deletedAt;

    @Builder
    public User(String email, String password, String selfIntroduction) {
        this.email = email;
        this.password = password;
        this.selfIntroduction = selfIntroduction;
    }

    public User(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public static User fromAuthUser(AuthUser authUser) {
        return new User(authUser.getId(), authUser.getEmail());
    }

    public void update(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void changeDeletedAt(LocalDateTime localDateTime) {
        this.deletedAt = localDateTime;
    }
}
