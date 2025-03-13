package com.example.developednewsfeed.post.repository;

public interface PostRepositoryCustom {

    // deletedAt Filter 활성화 메서드
    void enableSoftDeleteFilter();
    // deletedAt Filter 비활성화 메서드
    void disableSoftDeleteFilter();
}
