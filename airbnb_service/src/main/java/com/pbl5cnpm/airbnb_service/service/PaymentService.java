package com.pbl5cnpm.airbnb_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pbl5cnpm.airbnb_service.repository.PaymentRepository;
import com.pbl5cnpm.airbnb_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    
    public Long counts(){
        return this.paymentRepository.count();
    }
    public Double getTotePayment(){
        return this.paymentRepository.getTotalRevenue();
    }
}
