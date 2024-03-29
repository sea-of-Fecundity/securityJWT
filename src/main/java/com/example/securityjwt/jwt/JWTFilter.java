package com.example.securityjwt.jwt;

import com.example.securityjwt.exception.token.AccessTokenNotFoundException;
import com.example.securityjwt.exception.token.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = request.getHeader("access");
        if (accessToken == null) {
//            throw new AccessTokenNotFoundException();
            return;
        }

        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {
            throw new AccessTokenNotFoundException();
        }


        else if (jwtUtil.isExpired(accessToken)) {
            throw new TokenExpiredException();
        }

        String address = jwtUtil.getAddress(accessToken);
        String role = jwtUtil.getRole(accessToken);


//        new UsernamePasswordAuthenticationToken();


    }
}