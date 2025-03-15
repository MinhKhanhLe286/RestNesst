package com.pbl5cnpm.airbnb_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pbl5cnpm.airbnb_service.AirbnbServiceApplication;
import com.pbl5cnpm.airbnb_service.dto.Request.CoutriesRequest;
import com.pbl5cnpm.airbnb_service.dto.Response.ApiResponse;
import com.pbl5cnpm.airbnb_service.dto.Response.CoutriesResponse;
import com.pbl5cnpm.airbnb_service.service.CoutriesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")

public class CountriesController {

    private final CoutriesService coutriesService;

    CountriesController(CoutriesService coutriesService) {
        this.coutriesService = coutriesService;
    }
    @PostMapping("/countries")
    public ResponseEntity<ApiResponse<CoutriesResponse>> createCountries(@RequestBody CoutriesRequest coutriesRequest){
        ApiResponse<CoutriesResponse> apiResponse = ApiResponse.<CoutriesResponse>builder()
                                                    .result(this.coutriesService.handleCreateCounties(coutriesRequest))
                                                    .code(201)
                                                    .message("create country success!")
                                                    .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}
