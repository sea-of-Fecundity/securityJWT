package com.example.securityjwt.exception.token;

public class RefreshTokenNotFoundException extends TokenException{


    private final static String MESSAGE = "Not Found Refresh Token";
    public RefreshTokenNotFoundException() {
        super();
    }

    public RefreshTokenNotFoundException(String message) {
        super(message);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
