package com.bancrabs.villaticket.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.bancrabs.villaticket.services.TokenService;

@Configuration
@EnableScheduling
public class ScheduledTaskConfiguration {
    
    @Autowired
    private TokenService tokenService;

    @Scheduled(cron = "0 0 3 1 * ?")
    public void clearToken() {
        tokenService.cleanInactiveTokens();
    }

}