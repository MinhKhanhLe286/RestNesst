package com.pbl5cnpm.airbnb_service.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pbl5cnpm.airbnb_service.dto.Response.AmenitiesResponse;
import com.pbl5cnpm.airbnb_service.service.AmenitiesService;
import com.pbl5cnpm.airbnb_service.service.ImageService;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class Hello {
    @Autowired
    private ImageService imageService;
    @Autowired
    private AmenitiesService amenitiesService;
    @GetMapping("/hello")
    public List<AmenitiesResponse> getALL(){
        return this.amenitiesService.handleGetALlAmenities();
    }
    // public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
    //     try {
    //         // Gọi service để xử lý việc lưu ảnh
    //         this.imageService.handleSaveImage(file);
    //         return ResponseEntity.status(HttpStatus.CREATED).body("Image uploaded successfully!");
    //     } catch (IOException e) {
    //         // Xử lý lỗi nếu có
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image: " + e.getMessage());
    //     }
    // }
    
}
