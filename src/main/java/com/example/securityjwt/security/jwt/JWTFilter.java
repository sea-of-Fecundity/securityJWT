package com.example.securityjwt.security.jwt;

import com.example.securityjwt.exception.token.AccessTokenExpiredException;
import com.example.securityjwt.exception.token.AccessTokenInvalidException;
import com.example.securityjwt.security.CustomUserDetails;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ContentHandler;

import static com.example.securityjwt.config.properties.TokenProperties.ACCESS_TOKEN_NAME;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        SecurityContext context = SecurityContextHolder.getContext();
        log.info("JWTFilter 시작점에서의 SecurityContext 확인 :{} ", context);

        String accessToken = request.getHeader(ACCESS_TOKEN_NAME);

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            throw new AccessTokenExpiredException();
        }

        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals(ACCESS_TOKEN_NAME)) {
            throw new AccessTokenInvalidException();
        }

        String address = jwtUtil.getAddress(accessToken);
        String role = jwtUtil.getRole(accessToken);

        CustomUserDetails build = CustomUserDetails.builder()
                .address(address)
                .role(role)
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(build,
                null,
                build.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("JWTFilter 작동 후 SecurityContext 확인 :{} ", context);
        filterChain.doFilter(request, response);

    }
}