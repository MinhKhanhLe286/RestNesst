package com.pbl5cnpm.airbnb_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pbl5cnpm.airbnb_service.dto.Response.AmenitiesResponse;
import com.pbl5cnpm.airbnb_service.dto.Response.ApiResponse;
import com.pbl5cnpm.airbnb_service.service.AmenitiesService;

@RestController
public class AmenitiesController {
    @Autowired
    private AmenitiesService amenitiesService;
    @GetMapping("/api/amenities")
    public ApiResponse<List<AmenitiesResponse>> getAll(){
        var result = this.amenitiesService.handleGetALlAmenities();
        return ApiResponse.<List<AmenitiesResponse>>builder()
                            .result(result)
                            .message("Get amenities successfully!")
                            .code(200)
                            .build();
    }
}
