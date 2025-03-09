package com.pbl5cnpm.airbnb_service.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class Hello {
    @GetMapping("/hello")
    public String getMethodName() {
        return "hello";
    }
    
}
