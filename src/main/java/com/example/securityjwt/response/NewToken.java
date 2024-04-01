package com.example.securityjwt.response;

import jakarta.servlet.http.Cookie;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NewToken {

    private final String newAccessToken;
    private final Cookie newCookie;

    @Builder
    public NewToken(String newAccessToken, Cookie newCookie) {
        this.newAccessToken = newAccessToken;
        this.newCookie = newCookie;
    }
}
