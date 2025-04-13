package com.pbl5cnpm.airbnb_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pbl5cnpm.airbnb_service.entity.ListingEntity;
import com.pbl5cnpm.airbnb_service.repository.Custom.ListingsRepositoryCustom;

@Repository
public interface ListingsRepository extends JpaRepository<ListingEntity, Long>, ListingsRepositoryCustom {
    List<ListingEntity> findAll();
}
