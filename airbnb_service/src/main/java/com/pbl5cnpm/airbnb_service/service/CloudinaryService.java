package com.pbl5cnpm.airbnb_service.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public String uploadImageCloddy(MultipartFile file) {
        try {
            Map upload = cloudinary.uploader().upload(
                file.getBytes(), // Dùng getBytes()
                ObjectUtils.emptyMap() // Để SDK tự động xử lý chữ ký
            );
            return (String) upload.get("url");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

