package com.pbl5cnpm.airbnb_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pbl5cnpm.airbnb_service.entity.PaymentEntity;
import com.pbl5cnpm.airbnb_service.repository.PaymentRepository;
import com.pbl5cnpm.airbnb_service.repository.UserRepository;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UserRepository userRepository;
    public Long counts(){
        return this.paymentRepository.count();
    }
}
