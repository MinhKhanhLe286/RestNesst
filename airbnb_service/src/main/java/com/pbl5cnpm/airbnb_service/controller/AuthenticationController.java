package com.pbl5cnpm.airbnb_service.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;
import com.pbl5cnpm.airbnb_service.dto.Request.AuthenticationResquest;
import com.pbl5cnpm.airbnb_service.dto.Request.IntrospectRequest;
import com.pbl5cnpm.airbnb_service.dto.Response.ApiResponse;
import com.pbl5cnpm.airbnb_service.dto.Response.AuthenticationResponse;
import com.pbl5cnpm.airbnb_service.dto.Response.IntrospectResponse;
import com.pbl5cnpm.airbnb_service.service.AuthenticationService;

@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping("auth/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationResquest resquest) throws JOSEException{
        var result = this.authenticationService.authenticate(resquest);
        return  ApiResponse.<AuthenticationResponse>builder()
                            .result(result)
                            .code(200)
                            .message("Login succesfully!")
                            .build();
    }
    @PostMapping("auth/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest introspectRequest) throws JOSEException, ParseException{

        return ApiResponse.<IntrospectResponse>builder()
                        .result(this.authenticationService.introspect(introspectRequest))
                        .code(200)
                        .message("Introspect valid!")
                        .build();   
    }
}
