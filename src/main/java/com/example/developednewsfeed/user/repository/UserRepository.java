package com.example.developednewsfeed.user.repository;

import com.example.developednewsfeed.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
