package com.example.securityjwt.exception.token;

public class RefreshTokenNotFoundException extends TokenException{


    private final static String MESSAGE = "refresh token을 찾지 못했습니다.";
    public RefreshTokenNotFoundException() {
        super();
    }

    public RefreshTokenNotFoundException(String message) {
        super(message);
    }
}
