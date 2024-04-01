package com.example.securityjwt.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenProperties {

    @Value("${security.token.access.expirationMinutes}")
    private Long accessTokenExpirationMinutes;

    @Value("${security.token.refresh.expirationDays}")
    private Long refreshTokenExpirationDays;

    public Long getAccessTokenExpirationMinutes() {
        return accessTokenExpirationMinutes * 60000;
    }

    public Long getRefreshTokenExpirationDays() {
        return refreshTokenExpirationDays * 86400000;
    }
}
