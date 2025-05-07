package com.pbl5cnpm.airbnb_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pbl5cnpm.airbnb_service.dto.Request.FavoriteRequest;
import com.pbl5cnpm.airbnb_service.dto.Response.ApiResponse;
import com.pbl5cnpm.airbnb_service.service.FavoriteService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;
    @PostMapping("/favorites")
    public ResponseEntity<Void> postMethodName(@RequestBody FavoriteRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username =  authentication.getName();
        Long listingId =  request.getListingId();
        this.favoriteService.addFavorite(listingId, username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @DeleteMapping("/favorites")
    public ApiResponse<Void> handleDelete(@RequestBody FavoriteRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username =  authentication.getName();
        Long listingId =  request.getListingId();
        this.favoriteService.deleteFavorite(listingId, username);
    
        return ApiResponse.<Void>builder()
                .code(200)
                .message("delete succesfully")
                .build() ;
    }
}
