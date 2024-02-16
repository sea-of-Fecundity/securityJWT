package com.example.securityjwt.exception.token;

public class TokenExpiredException extends TokenException{

    private final static String MESSAGE = "Expired Token";
    public TokenExpiredException() {
        super(MESSAGE);
    }
}
