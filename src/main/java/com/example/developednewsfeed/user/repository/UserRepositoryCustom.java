package com.example.developednewsfeed.user.repository;

// soft delete 구현을 위한 custom repository
public interface UserRepositoryCustom {

    // deletedAt Filter 활성화 메서드
    void enableSoftDeleteFilter();
    // deletedAt Filter 비활성화 메서드
    void disableSoftDeleteFilter();
}
