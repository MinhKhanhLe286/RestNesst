package com.pbl5cnpm.airbnb_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class CloudinaryConfig {
    // @Value("${cloudinary.cloudName}")
    // private String cloudName;

    // @Value("${cloudinary.apiKey}")
    // private String apiKey;

    // @Value("${cloudinary.apiSecret}")
    // private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
       Dotenv dotenv = Dotenv.load();
       return new Cloudinary(dotenv.get("CLOUDINARY_URL"));
    }
}

