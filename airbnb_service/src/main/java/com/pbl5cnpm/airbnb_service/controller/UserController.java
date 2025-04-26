package com.pbl5cnpm.airbnb_service.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;
import com.pbl5cnpm.airbnb_service.dto.Request.UserRequest;
import com.pbl5cnpm.airbnb_service.dto.Response.ApiResponse;
import com.pbl5cnpm.airbnb_service.dto.Response.UserInfor;
import com.pbl5cnpm.airbnb_service.dto.Response.UserResponse;
import com.pbl5cnpm.airbnb_service.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")

public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserResponse>> created(@RequestBody @Valid UserRequest request)  {
        UserResponse userResponse = userService.handleCreateUser(request);

        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code(201)
                .message("Created user successfully!")
                .result(userResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getALL() {
        List<UserResponse> userResponses = this.userService.handleGetAll();

        ApiResponse<List<UserResponse>> apiResponse = ApiResponse.<List<UserResponse>>builder()
                .code(200)
                .message("Fetched users successfully!")
                .result(userResponses)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
    
    @GetMapping("/users/myInformation")
    public ApiResponse getMethodName(HttpServletRequest  request) throws ParseException, JOSEException {
        String authorization = request.getHeader("Authorization");
        String token = "";
        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        }
        return ApiResponse.<UserInfor>builder()
                    .result(this.userService.handleInfor(token))
                    .build();
    }

}
