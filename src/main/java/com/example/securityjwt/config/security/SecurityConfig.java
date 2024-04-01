package com.example.securityjwt.config.security;


import com.example.securityjwt.config.properties.TokenProperties;
import com.example.securityjwt.jwt.JWTFilter;
import com.example.securityjwt.jwt.JwtUtil;
import com.example.securityjwt.repository.RefreshRepository;
import com.example.securityjwt.service.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${login.url}")
    private String login_url;

    @Value("${front.url}")
    private String front_url;

    private final RefreshTokenService refreshTokenService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final TokenProperties tokenProperties;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .sessionManagement((auth) -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.
            securityMatcher("/login", "/logout")
                .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/join", "/").permitAll()
                .requestMatchers("/admin", "/myPage").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/reissue").permitAll()
                .anyRequest().permitAll());

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        http
                .addFilterAt(LoginFilter.builder()
                    .defaultFilterProcessesUrl(login_url)
                    .objectMapper(objectMapper)
                    .jwtUtil(jwtUtil)
                    .authenticationManager(authenticationManager(authenticationConfiguration))
                    .refreshTokenService(refreshTokenService)
                    .tokenProperties(tokenProperties)
                    .build(),
                        UsernamePasswordAuthenticationFilter.class);

        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        http
                .addFilterBefore(CustomLogoutFilter.builder()
                        .jwtUtil(jwtUtil)
                        .refreshTokenService(refreshTokenService)
                        .build(), LogoutFilter.class);

        http
                .cors((corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(Collections.singletonList(front_url));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);
                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                    return configuration;
                })));

        return http.build();
    }
}