package com.pbl5cnpm.airbnb_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pbl5cnpm.airbnb_service.dto.Request.ListingRequest;
import com.pbl5cnpm.airbnb_service.dto.Response.ApiResponse;
import com.pbl5cnpm.airbnb_service.dto.Response.ListingDetailResponse;
import com.pbl5cnpm.airbnb_service.dto.Response.ListingsResponse;
import com.pbl5cnpm.airbnb_service.service.ListingsServices;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("${api.base.path}")
@RequiredArgsConstructor
public class ListingsController {
    private final ListingsServices listingsServices;

    @GetMapping("/listings")
    public ApiResponse<List<ListingsResponse>> getAll() {
        ApiResponse apiResponse = ApiResponse.<List<ListingsResponse>>builder()
                .code(200)
                .message("feetch listing successfuly!")
                .result(this.listingsServices.handleGetAll())
                .build();
        return apiResponse;
    }

    @GetMapping("/listings/{id}")
    public ApiResponse<ListingDetailResponse> getDetail(@PathVariable Long id) {
        return ApiResponse.<ListingDetailResponse>builder()
                .code(200)
                .message("get detail successfully!")
                .result(this.listingsServices.getDetail(id))
                .build();
    }

    @PreAuthorize("hasAuthority('HOST') or hasAuthority('ADMIN')")
    @PostMapping(value = "/listings", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ListingsResponse>> creaeListing(@ModelAttribute ListingRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ApiResponse<ListingsResponse> apiResponse = ApiResponse.<ListingsResponse>builder()
                .message("Create listing successfully")
                .code(201)
                .result(this.listingsServices.handlleCreate(request, username))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/listings/counts")
    public ApiResponse<Long> getcounts() {
        return ApiResponse.<Long>builder()
                        .code(200)
                        .message("get count listing")
                        .result(this.listingsServices.getCount())
                        .build();
    }
    

}
