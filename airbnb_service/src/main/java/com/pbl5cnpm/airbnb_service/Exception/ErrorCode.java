package com.pbl5cnpm.airbnb_service.exception;

public enum ErrorCode {
    USER_EXISTED(1000,"user existd"),
    ROLE_NOT_EXISTED(1001,"role not exited!"),
    USERNAME_EXISTED(1002, "username exited!"),
    USERNAME_VALID(1003,"username is vaild!"),
    PASSWORD_VALID(1004,"password is vaild!"),
    INVALID_KEY(105, "invalid key"),
    UNAUTHENTICATED(106, "unauthenticated!"),
    COUNTRY_EXISTED(1007,"countriy existed!")
    ;
    ErrorCode(int code, String message){
        this.code = code;
        this.message = message;     
    }
    private int code;
    private String message;
    
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
}
