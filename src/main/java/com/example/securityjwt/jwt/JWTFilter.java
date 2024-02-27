package com.example.securityjwt.jwt;

import com.example.securityjwt.domain.Member;
import com.example.securityjwt.exception.token.TokenExpiredException;
import com.example.securityjwt.exception.token.TokenNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

            String authorization = request.getHeader("Authorization");

            if (authorization == null || !authorization.startsWith("Bearer ")) {
                throw new TokenNotFoundException();
            }

            String token = authorization.split(" ")[1];


            if (jwtUtil.isExpired(token)) {
                throw new TokenExpiredException();
            }

            String address = jwtUtil.getAddress(token);
            String role = jwtUtil.getRole(token);

            Member build = Member.builder()
                    .address(address)
                    .role(role)
                    .userName("user1")
                    .password("testPassword")
                    .build();

            CustomUserDetails customUserDetails = new CustomUserDetails(build);

            //스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            //세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
    }
}