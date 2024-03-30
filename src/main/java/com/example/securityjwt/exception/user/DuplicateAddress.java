package com.example.securityjwt.exception.user;

public class DuplicateAddress extends RuntimeException{

    private final static String MESSAGE = "중복된 이메일입니다.";

    public DuplicateAddress(String message) {
    super(MESSAGE);
    }


    public DuplicateAddress() {
        super(MESSAGE);
    }

    public DuplicateAddress(String message, Throwable cause) {
        super(message, cause);
    }
}
