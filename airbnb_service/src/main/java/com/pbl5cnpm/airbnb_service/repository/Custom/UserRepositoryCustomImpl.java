package com.pbl5cnpm.airbnb_service.repository.Custom;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pbl5cnpm.airbnb_service.entity.ListingEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ListingEntity> findFavorites(Long userId) {
        String sql = "SELECT l.* FROM favorites f " +
                     "JOIN listings l ON f.listing_id = l.id " +
                     "WHERE f.user_id = :userId AND f.deteted = false";

        Query query = entityManager.createNativeQuery(sql, ListingEntity.class);
        query.setParameter("userId", userId);

        return query.getResultList();
    }
}
