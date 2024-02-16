package com.example.securityjwt.exception.token;

public class TokenNotFoundException extends TokenException{

    private final static String MESSAGE = "Not Found Token!";

    public TokenNotFoundException() {
        super(MESSAGE);
    }
}
