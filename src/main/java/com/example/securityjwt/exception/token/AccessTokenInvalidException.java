package com.example.securityjwt.exception.token;

public class AccessTokenInvalidException extends TokenException{

    private final static String MESSAGE = "Invalid Access Token";

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
