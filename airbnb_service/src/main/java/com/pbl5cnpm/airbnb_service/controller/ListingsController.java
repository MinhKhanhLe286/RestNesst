package com.pbl5cnpm.airbnb_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pbl5cnpm.airbnb_service.dto.Response.ApiResponse;
import com.pbl5cnpm.airbnb_service.dto.Response.ListingDetailResponse;
import com.pbl5cnpm.airbnb_service.dto.Response.ListingsResponse;
import com.pbl5cnpm.airbnb_service.service.ListingsServices;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.base.path}")
@RequiredArgsConstructor
public class ListingsController {
    private final ListingsServices listingsServices;
    @GetMapping("/listings")
    public ApiResponse<List<ListingsResponse>> getAll(){
        ApiResponse apiResponse = ApiResponse.<List<ListingsResponse>>builder()
                                .code(200)
                                .message("feetch listing successfuly!")
                                .result(this.listingsServices.handleGetAll())
                                .build();
        return apiResponse;
    }
    @GetMapping("/listings/{id}")
    public ApiResponse<ListingDetailResponse> getDetail(@PathVariable Long id){
        return ApiResponse.<ListingDetailResponse>builder()
                .code(200)
                .message("get detail successfully!")
                .result(this.listingsServices.getDetail(id))
                .build();
    }
}
