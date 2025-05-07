package com.pbl5cnpm.airbnb_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pbl5cnpm.airbnb_service.entity.CreatePayment;

@Repository
public interface CreatePaymentRepository extends JpaRepository<CreatePayment, String> {
    
}
