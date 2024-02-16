package com.example.securityjwt.exception.Login;

public class LoginFailedException extends LoginException{

    private final static String MESSAGE = "Login Failed";

    public LoginFailedException() {
        super(MESSAGE);
    }

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
