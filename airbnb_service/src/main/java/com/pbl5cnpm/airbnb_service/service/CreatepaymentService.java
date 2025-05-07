package com.pbl5cnpm.airbnb_service.service;

import org.springframework.stereotype.Service;

import com.pbl5cnpm.airbnb_service.dto.Request.CreatePaymentRequest;
import com.pbl5cnpm.airbnb_service.entity.CreatePaymentInfo;
import com.pbl5cnpm.airbnb_service.entity.ListingEntity;
import com.pbl5cnpm.airbnb_service.exception.AppException;
import com.pbl5cnpm.airbnb_service.exception.ErrorCode;
import com.pbl5cnpm.airbnb_service.repository.CreateInfoPaymentRepository;
import com.pbl5cnpm.airbnb_service.repository.ListingsRepository;
import com.pbl5cnpm.airbnb_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreatepaymentService {
    private final CreateInfoPaymentRepository createPaymentInfoRepository;
    private final UserRepository userRepository;
    private final ListingsRepository listingsRepository;
    public String createPaymentInfo(CreatePaymentRequest request, String username){
        
        ListingEntity entityListing = this.listingsRepository.findById(request.getListingId())  
                                .orElseThrow(()-> new AppException(ErrorCode.INVALID));

        Long userId = this.userRepository.findByUsername(username).get().getId();
        CreatePaymentInfo payment = new CreatePaymentInfo().builder()
                            .userId(userId)
                            .listingId(request.getListingId())
                            .startDate(request.getStartDate())
                            .endDate(request.getEndDate())
                            .amount(request.getAmount())
                            .build();
        String infoID = this.createPaymentInfoRepository.save(payment).getId();
        return infoID;
    }
}
