package com.example.securityjwt.service;

import com.example.securityjwt.config.properties.TokenProperties;
import com.example.securityjwt.domain.RefreshToken;
import com.example.securityjwt.exception.token.RefreshTokenNotFoundException;
import com.example.securityjwt.security.jwt.JwtUtil;
import com.example.securityjwt.repository.RefreshRepository;
import com.example.securityjwt.response.NewToken;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

import static com.example.securityjwt.config.properties.TokenProperties.ACCESS_TOKEN_NAME;
import static com.example.securityjwt.config.properties.TokenProperties.REFRESH_TOKEN_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshRepository refreshRepository;
    private final JwtUtil jwtUtil;
    private final TokenProperties tokenProperties;


    public Boolean checkRefreshToken(String refresh) {
        return refreshRepository.existsByRefresh(refresh);
    }


    public void deleteRefreshToken(String refresh) {
        refreshRepository.deleteByRefresh(refresh);
    }


    public NewToken checkCookie(Cookie[] cookies) {

        String refresh = Arrays.stream(cookies)
                .filter(cookie -> REFRESH_TOKEN_NAME.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(RefreshTokenNotFoundException::new);
        log.info("refresh{}", refresh);
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        String category = jwtUtil.getCategory(refresh);

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenNotFoundException();
        }

        if (!category.equals(REFRESH_TOKEN_NAME) || !isExist) {
            throw new RefreshTokenNotFoundException();
        }

        String address = jwtUtil.getAddress(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccess = jwtUtil
                .createJwt(ACCESS_TOKEN_NAME, address, role,
                        tokenProperties.getAccessTokenExpirationMinutes());
        String newRefresh = jwtUtil
                .createJwt(REFRESH_TOKEN_NAME, address, role,
                        tokenProperties.getRefreshTokenExpirationDays());

        refreshRepository.deleteByRefresh(refresh);

        addRefreshEntity(address, refresh, tokenProperties.getRefreshTokenExpirationDays());
        Cookie newCookies = createCookies(REFRESH_TOKEN_NAME, newRefresh);

        return NewToken.builder()
                .newAccessToken(newAccess)
                .newCookie(newCookies)
                .build();
    }


    private void addRefreshEntity(String address, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        RefreshToken refreshTokenDomain = RefreshToken.builder()
                .userAddress(address)
                .refresh(refresh)
                .expired(date.getTime())
                .build();

        save(refreshTokenDomain);
    }


    private Cookie createCookies(String key, String value){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);
        return cookie;
    }


    public void save(RefreshToken refreshToken) {
        if(!refreshRepository.existsByUserAddress(refreshToken.getUserAddress())) {
            refreshRepository.save(refreshToken);
        }
    }


    public void deleteExpiredRefreshToken() {
        refreshRepository.findAll().stream()
                .filter((refreshToken) -> refreshToken.getExpired() <= System.currentTimeMillis())
                .forEach((domain) -> refreshRepository.deleteById(domain.getId()));
    }

    public void deleteAll() {
        refreshRepository.deleteAll();

    }
}
