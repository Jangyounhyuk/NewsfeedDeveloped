package com.example.developednewsfeed.user.entity;

import com.example.developednewsfeed.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Column(unique = true, nullable = false)
    private String email;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*()-+=]).{8,}$", message = "비밀번호는 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함하며 8자 이상이어야 합니다.")
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
