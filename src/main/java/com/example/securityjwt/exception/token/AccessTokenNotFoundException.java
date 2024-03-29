package com.example.securityjwt.exception.token;

public class AccessTokenNotFoundException extends TokenException{

    private final static String MESSAGE = "Not Found Assess Token!";

    public AccessTokenNotFoundException() {
        super(MESSAGE);
    }
}
