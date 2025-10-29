package com.kakaotechbootcamp.community.utils;


import com.kakaotechbootcamp.community.auth.SessionStorage;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionCleanupScheduler {

    private final SessionStorage sessionStorage;

    @Scheduled(fixedDelay = 60 * 60 * 3, timeUnit = TimeUnit.SECONDS)
    public void cleanupExpiredSessions() {
        sessionStorage.cleanup();
    }
}
