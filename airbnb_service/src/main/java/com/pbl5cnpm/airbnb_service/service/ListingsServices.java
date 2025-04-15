package com.pbl5cnpm.airbnb_service.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pbl5cnpm.airbnb_service.dto.Response.ListingDetailResponse;
import com.pbl5cnpm.airbnb_service.dto.Response.ListingsResponse;
import com.pbl5cnpm.airbnb_service.entity.ListingEntity;
import com.pbl5cnpm.airbnb_service.exception.AppException;
import com.pbl5cnpm.airbnb_service.exception.ErrorCode;
import com.pbl5cnpm.airbnb_service.mapper.ListingMapper;
import com.pbl5cnpm.airbnb_service.repository.ListingsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListingsServices {
    private final ListingsRepository listingsRepository;
    private final ListingMapper listingMapper;
    public List<ListingsResponse> handleGetAll(){
        List<ListingEntity> entitys = this.listingsRepository.findAllAndStatus(true, false, true, LocalDate.now());
        
        return entitys.stream().map(listingMapper::toResponse).toList();
    }
    public ListingDetailResponse getDetail(Long id) {
        ListingEntity entity = this.listingsRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_EXISTED));
        return this.listingMapper.toDetailResponse(entity);
    }
    
}
