package com.pbl5cnpm.airbnb_service.service;

import org.springframework.stereotype.Service;

import com.pbl5cnpm.airbnb_service.dto.Request.CreatePaymentRequest;
import com.pbl5cnpm.airbnb_service.entity.CreatePayment;
import com.pbl5cnpm.airbnb_service.repository.CreatePaymentRepository;
import com.pbl5cnpm.airbnb_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreatepaymentService {
    private final CreatePaymentRepository createPaymentInfoRepository;
    private final UserRepository userRepository;
    public String createPaymentInfo(CreatePaymentRequest request, String username){
        Long userId = this.userRepository.findByUsername(username).get().getId();
        CreatePayment payment = new CreatePayment().builder()
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
