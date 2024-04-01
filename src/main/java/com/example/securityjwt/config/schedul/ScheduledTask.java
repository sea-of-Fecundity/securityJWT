package com.example.securityjwt.config.schedul;


import com.example.securityjwt.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTask {

    private final RefreshTokenService refreshTokenService;


    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void reportCurrentTime() {
        refreshTokenService.deleteExpiredRefreshToken();
    }
}
