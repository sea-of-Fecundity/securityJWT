package com.example.securityjwt.exception.token;

public class RefreshTokenInvalidException extends TokenException{
    private final static String MESSAGE = "Invalid Refresh Token";

    public RefreshTokenInvalidException() {
        super(MESSAGE);
    }

    public RefreshTokenInvalidException(String message) {
        super(message);
    }

    public RefreshTokenInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int statusCode() {
        return 401;
    }
}
