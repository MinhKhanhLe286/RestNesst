package com.pbl5cnpm.airbnb_service.repository.Custom;

import java.time.LocalDate;
import java.util.List;

import com.pbl5cnpm.airbnb_service.entity.ListingEntity;

public interface ListingsRepositoryCustom {
    List<ListingEntity> findAllAndStatus(boolean isActive, boolean deleted, boolean access, LocalDate now);
}
