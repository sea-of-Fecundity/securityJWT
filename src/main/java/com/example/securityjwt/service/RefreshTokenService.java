package com.example.securityjwt.service;

import com.example.securityjwt.config.properties.TokenProperties;
import com.example.securityjwt.domain.Refresh;
import com.example.securityjwt.exception.token.RefreshTokenNotFoundException;
import com.example.securityjwt.jwt.JwtUtil;
import com.example.securityjwt.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

import static com.example.securityjwt.config.properties.TokenProperties.ACCESS_TOKEN_NAME;
import static com.example.securityjwt.config.properties.TokenProperties.REFRESH_TOKEN_NAME;

@Service
@RequiredArgsConstructor
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


    public ResponseEntity<String> checkCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        String refresh = Arrays.stream(cookies)
                .filter(cookie -> REFRESH_TOKEN_NAME.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(RefreshTokenNotFoundException::new);

        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        String category = jwtUtil.getCategory(refresh);

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        if (!category.equals(REFRESH_TOKEN_NAME) || !isExist) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
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

        response.setHeader(ACCESS_TOKEN_NAME, newAccess);
        response.addCookie(createCookies(REFRESH_TOKEN_NAME, newRefresh));
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private void addRefreshEntity(String address, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        Refresh refreshEntity = Refresh.builder()
                .userAddress(address)
                .refresh(refresh)
                .expired(date.getTime())
                .build();

        save(refreshEntity);
    }


    private Cookie createCookies(String key, String value){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);
        return cookie;
    }


    public void save(Refresh refresh) {
        refreshRepository.save(refresh);
    }


    public void deleteExpiredRefreshToken() {
        refreshRepository.findAll().stream()
                .filter((refresh) -> refresh.getExpired() <= System.currentTimeMillis())
                .forEach((domain) -> refreshRepository.deleteById(domain.getId()));
    }
}
