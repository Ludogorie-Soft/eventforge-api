package com.eventforge.scheduler;

import com.eventforge.model.Token;
import com.eventforge.model.User;
import com.eventforge.repository.TokenRepository;
import com.eventforge.repository.UserRepository;
import com.eventforge.security.jwt.JWTService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final JWTService jwtService;

    @Scheduled(cron = "0 0 * * * *") // Run every hour at the start of the hour
    public void tokenExpirationTimeInspection() {
        List<Token> tokensToInspect = tokenRepository.getAllUnexpiredTokensForInspection();
        if (tokensToInspect != null && !tokensToInspect.isEmpty()) {
            for (Token token : tokensToInspect) {
                try {
                    jwtService.isTokenExpired(token.getTokenValue());
                } catch (ExpiredJwtException e) {
                    token.setRevoked(true);
                    token.setExpired(true);
                    tokenRepository.save(token);
                }
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void clearUnverifiedAccounts() {
        LocalDateTime cutoffDateTime = LocalDateTime.now().minus(Duration.ofDays(7));
        List<User> unverifiedAccounts = userRepository.getUnverifiedAccountsOlderThan(cutoffDateTime);

        for (User unverifiedUser : unverifiedAccounts) {
            LocalDateTime createdAt = unverifiedUser.getRegisteredAt();
            if ((!unverifiedUser.getIsEnabled()) && createdAt.isBefore(cutoffDateTime)) {
                // Double-Check if the account has remained unverified for more than 7 days
                userRepository.delete(unverifiedUser);
            }
        }
    }
}
