package com.pbl5cnpm.airbnb_service.repository.Custom;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pbl5cnpm.airbnb_service.entity.ListingEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class ListingsRepositoryCustomImpl implements ListingsRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ListingEntity> findAllAndStatus(boolean isActive, boolean deleted, boolean access) {
        String sql = "SELECT * FROM listings WHERE is_active = :isActive AND deleted = :deleted AND access = :access";
        Query query = entityManager.createNativeQuery(sql, ListingEntity.class);
        query.setParameter("isActive", isActive);
        query.setParameter("deleted", deleted);
        query.setParameter("access", access);
        return query.getResultList();
    }

}
