package com.example.developednewsfeed.user.entity;

import com.example.developednewsfeed.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "\"user\"")
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
}
