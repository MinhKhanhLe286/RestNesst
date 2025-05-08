package com.pbl5cnpm.airbnb_service.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_EXISTED(1000,"user existd", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_EXISTED(1009,"user not existd",   HttpStatus.INTERNAL_SERVER_ERROR),
    ROLE_NOT_EXISTED(1001,"role not exited!",  HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_EXISTED(1002, "username exited!",  HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_VALID(1003,"username is vaild!",  HttpStatus.INTERNAL_SERVER_ERROR),
    PASSWORD_VALID(1004,"password is vaild!",  HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(105, "invalid key",  HttpStatus.INTERNAL_SERVER_ERROR),
    COUNTRY_EXISTED(1007,"countriy existed!", HttpStatus.INTERNAL_SERVER_ERROR),
    LISTING_NOT_EXISTED(1008,"listing not existed!",  HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(106, "unauthenticated! || token is invalid ", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1010, "user have no permisson!", HttpStatus.FORBIDDEN),
    INVALID(1011, "data invalid", HttpStatus.BAD_REQUEST),
    FAVORITE_NOT_EXISTED(1012, "favorite not exited", HttpStatus.BAD_REQUEST),
    TRANSACTION_EXISTED(1013, "transaction exited", HttpStatus.BAD_REQUEST),
    COUNTRY_NOT_EXISTED(1014,"countriy not existed!", HttpStatus.BAD_REQUEST),
    ;
    ErrorCode(int code, String message, HttpStatus httpStatus){
        this.code = code;
        this.message = message;     
        this.httpStatus = httpStatus;
    }
    private int code;
    private String message;
    private HttpStatus httpStatus;
    
}
