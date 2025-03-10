package com.pbl5cnpm.airbnb_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.pbl5cnpm.airbnb_service.dto.Request.CategoriesRequest;
import com.pbl5cnpm.airbnb_service.dto.Response.ApiResponse;
import com.pbl5cnpm.airbnb_service.dto.Response.CategoriesResponse;
import com.pbl5cnpm.airbnb_service.service.CategoriesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class CategoriesController {
    @Autowired
    private CategoriesService categoriesService;
    @PostMapping("/api/categories")
    public ResponseEntity<ApiResponse<CategoriesResponse>> createCategories(@RequestBody CategoriesRequest request) {
        CategoriesResponse data = this.categoriesService.handleCreateCategories(request);
        ApiResponse<CategoriesResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(data);
        apiResponse.setCode(201);
        apiResponse.setMessage("Created categorry succcess!");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    
}
