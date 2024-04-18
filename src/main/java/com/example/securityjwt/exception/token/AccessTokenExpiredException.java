package com.example.securityjwt.exception.token;

public class AccessTokenExpiredException extends TokenException{

    private final static String MESSAGE = "Expired Access Token";

    public AccessTokenExpiredException() {
        super();
    }

    public AccessTokenExpiredException(String message) {
        super(message);
    }

    @Override
    public int statusCode() {
        return 401;
    }
}
