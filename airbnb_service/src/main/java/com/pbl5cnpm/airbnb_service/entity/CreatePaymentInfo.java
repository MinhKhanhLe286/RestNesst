package com.pbl5cnpm.airbnb_service.entity;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PaymentInfo")
public class CreatePaymentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    Long userId;
    Long listingId;
    Long amount;
    Date startDate;
    Date endDate;
    LocalDateTime createAt;
    String content;
}
