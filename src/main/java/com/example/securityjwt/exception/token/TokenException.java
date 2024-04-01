package com.example.securityjwt.exception.token;


public abstract class TokenException extends  RuntimeException{
    public TokenException() {
        super();
    }

    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int statusCode();
}
