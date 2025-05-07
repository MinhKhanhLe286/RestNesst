package com.pbl5cnpm.airbnb_service.repository.Custom;

import java.util.List;

import com.pbl5cnpm.airbnb_service.entity.ListingEntity;

public interface UserRepositoryCustom {
    List<ListingEntity> findFavorites(Long userId);
}
