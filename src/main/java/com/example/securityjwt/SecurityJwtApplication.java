package com.example.securityjwt;

import com.example.securityjwt.jwt.JwtUtil;
import com.example.securityjwt.service.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.Ref;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SecurityJwtApplication {


    private final RefreshTokenService refreshTokenService;

    public static void main(String[] args) {
        SpringApplication.run(SecurityJwtApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            refreshTokenService.deleteAll();
        } ;

    }
}
