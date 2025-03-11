package com.example.developednewsfeed.user.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom{

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void enableSoftDeleteFilter() {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("activeUserFilter");
    }

    @Override
    public void disableSoftDeleteFilter() {
        Session session = entityManager.unwrap(Session.class);
        session.disableFilter("activeUserFilter");
    }
}
