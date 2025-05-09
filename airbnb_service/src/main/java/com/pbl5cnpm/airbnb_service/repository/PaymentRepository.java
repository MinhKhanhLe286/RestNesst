package com.pbl5cnpm.airbnb_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pbl5cnpm.airbnb_service.entity.PaymentEntity;
import com.pbl5cnpm.airbnb_service.entity.UserEntity;

import java.util.List;


@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByTransactionId(String transactionId);
    
}
