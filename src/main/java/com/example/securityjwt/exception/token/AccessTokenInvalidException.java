package com.example.securityjwt.exception.token;

public class AccessTokenInvalidException extends TokenException{

    private final static String MESSAGE = "invalid refresh token";

    public AccessTokenInvalidException() {
        super(MESSAGE);
    }

    public AccessTokenInvalidException(String message) {
        super(message);
    }

    public AccessTokenInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int statusCode() {
        return 401;
    }
}
