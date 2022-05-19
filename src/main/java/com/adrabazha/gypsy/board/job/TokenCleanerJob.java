package com.adrabazha.gypsy.board.job;

import com.adrabazha.gypsy.board.token.service.TokenService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TokenCleanerJob {

    private final List<TokenService<?, ?>> tokenServiceList;

    public TokenCleanerJob(List<TokenService<?, ?>> tokenServiceList) {
        this.tokenServiceList = tokenServiceList;
    }

    @Scheduled(cron = "@midnight")
    public void dropExpiredTokens() {
        tokenServiceList.forEach(TokenService::cleanExpiredTokens);
    }
}
