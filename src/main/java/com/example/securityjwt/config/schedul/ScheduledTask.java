package com.example.securityjwt.config.schedul;


import com.example.securityjwt.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTask {

    private final RefreshTokenService refreshTokenService;



}
