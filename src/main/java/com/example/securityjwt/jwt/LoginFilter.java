package com.example.securityjwt.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;


@Slf4j
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;

    public LoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        super(defaultFilterProcessesUrl);
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {

        LoginJson loginJson = objectMapper.readValue(request.getInputStream(), LoginJson.class);
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken
                    .unauthenticated(loginJson.getAddress(),
                    loginJson.getPassword());
        log.info("이메일 = {}, 비밀번호 = {}", loginJson.address, loginJson.getPassword());

            // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.authenticationManager.authenticate(authRequest);
    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }

    @Getter
    private static class LoginJson {
        private String address;
        private String password;
    }
}
