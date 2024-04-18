package com.example.securityjwt.controller;


import com.example.securityjwt.security.jwt.JwtUtil;
import com.example.securityjwt.response.NewToken;
import com.example.securityjwt.service.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.securityjwt.config.properties.TokenProperties.ACCESS_TOKEN_NAME;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final JwtUtil jwtUtil;
    private  final RefreshTokenService refreshTokenService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        NewToken newToken = refreshTokenService.checkCookie(cookies);

        response.setHeader(ACCESS_TOKEN_NAME, newToken.getNewAccessToken());
        response.addCookie(newToken.getNewCookie());

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
