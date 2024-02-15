package com.example.securityjwt.exception.user;

public class DuplicateAddress extends RuntimeException{
    public DuplicateAddress(String message) {


    }

    public DuplicateAddress(String message, Throwable cause) {
        super(message, cause);
    }
}
