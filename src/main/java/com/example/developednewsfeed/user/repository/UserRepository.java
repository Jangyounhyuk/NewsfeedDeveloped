package com.example.developednewsfeed.user.repository;

import com.example.developednewsfeed.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findByIdWithFilter(@Param("id") Long id);

    // 삭제 요청 후 2주 지난 사용자 삭제
    List<User> findAllByDeletedAtBefore(LocalDateTime twoWeeksAgo);

    List<User> findByIdIn(List<Long> followingIds);
}
