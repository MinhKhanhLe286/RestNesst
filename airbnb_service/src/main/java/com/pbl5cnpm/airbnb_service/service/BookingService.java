package com.pbl5cnpm.airbnb_service.service;

import org.springframework.stereotype.Service;

import com.pbl5cnpm.airbnb_service.dto.Request.BookingRequest;
import com.pbl5cnpm.airbnb_service.dto.Response.BookingResponse;
import com.pbl5cnpm.airbnb_service.entity.BookingEntity;
import com.pbl5cnpm.airbnb_service.entity.UserEntity;
import com.pbl5cnpm.airbnb_service.mapper.BookingMapper;
import com.pbl5cnpm.airbnb_service.repository.BookingRepository;
import com.pbl5cnpm.airbnb_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserRepository userRepository;

    public BookingResponse handleBooking(BookingRequest bookingRequest, String username) {
        UserEntity userEntity = this.userRepository.findByUsername(username).get();

        BookingEntity bookingEntity = bookingMapper.toBookingEntity(bookingRequest);
        bookingEntity.setStatus("PENDING");
        bookingEntity.setUser(userEntity);
        BookingEntity savedEntity = bookingRepository.save(bookingEntity);

        return bookingMapper.toBookingResponse(savedEntity);
    }
}
