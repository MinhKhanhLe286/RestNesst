package com.pbl5cnpm.airbnb_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.pbl5cnpm.airbnb_service.dto.Response.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(RuntimeException runtimeErr){
        ApiResponse<String> apiresponse = new ApiResponse<>();
        apiresponse.setMessage(runtimeErr.getMessage());

        return ResponseEntity.badRequest().body(apiresponse);
    }
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<String>> handleAppException(AppException appException){
        ApiResponse<String> apirest = new ApiResponse<>();
        ErrorCode err = appException.getErrorcode();
        apirest.setCode(err.getCode());
        apirest.setMessage((err.getMessage()));

        return ResponseEntity.badRequest().body(apirest);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        String key = exception.getFieldError().getDefaultMessage();
        ErrorCode err = ErrorCode.INVALID_KEY;
        try {
            err = ErrorCode.valueOf(key);
        } catch (IllegalArgumentException e) {
            System.out.println("Crreatd emun by key error!");
        }

        return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
                                                .message(err.getMessage())
                                                .code(err.getCode())   
                                                .build());
    }
}
