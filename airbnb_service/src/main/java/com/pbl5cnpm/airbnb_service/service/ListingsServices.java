package com.pbl5cnpm.airbnb_service.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pbl5cnpm.airbnb_service.dto.Request.ListingRequest;
import com.pbl5cnpm.airbnb_service.dto.Response.ListingDetailResponse;
import com.pbl5cnpm.airbnb_service.dto.Response.ListingsResponse;
import com.pbl5cnpm.airbnb_service.entity.ListingEntity;
import com.pbl5cnpm.airbnb_service.entity.UserEntity;
import com.pbl5cnpm.airbnb_service.enums.ListingStatus;
import com.pbl5cnpm.airbnb_service.exception.AppException;
import com.pbl5cnpm.airbnb_service.exception.ErrorCode;
import com.pbl5cnpm.airbnb_service.mapper.ListingMapper;
import com.pbl5cnpm.airbnb_service.repository.ListingsRepository;
import com.pbl5cnpm.airbnb_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListingsServices {
    private final ListingsRepository listingsRepository;
    private final ListingMapper listingMapper;
    private final UserRepository userRepository;
    public List<ListingsResponse> handleGetAll(){
        List<ListingEntity> entitys = this.listingsRepository.findAllAndStatus(ListingStatus.ACTIVE.toString(), false, true, LocalDate.now());
        
        return entitys.stream().map(listingMapper::toResponse).toList();
    }
    public ListingDetailResponse getDetail(Long id) {
        ListingEntity entity = this.listingsRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_EXISTED));
        return this.listingMapper.toDetailResponse(entity);
    }
    public ListingsResponse handlleCreate(ListingRequest listingRequest, String username){
        UserEntity host = this.userRepository.findByUsername(username)
                        .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));

        ListingEntity entity = this.listingMapper.toEntity(listingRequest);
        entity.setHost(host);
        entity.setDeleted(false);
        entity.setStatus(ListingStatus.ACTIVE.toString());
        if(host.getRoles().contains("ADMIN")) entity.setAccess(true);
        var enti =  this.listingsRepository.save(entity);
        return this.listingMapper.toResponse(enti);
    }
}
