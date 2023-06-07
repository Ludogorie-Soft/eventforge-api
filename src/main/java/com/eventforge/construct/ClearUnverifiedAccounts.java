package com.eventforge.construct;

import com.eventforge.model.User;
import com.eventforge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ClearUnverifiedAccounts {

    private final UserRepository userRepository;


    @Scheduled(cron = "0 0 0 * * *")
    public void deleteUnverifiedAccounts(){
        LocalDateTime cutoffDateTime = LocalDateTime.now().minus(Duration.ofDays(7));
        List<User> unverifiedAccounts = userRepository.getUnverifiedAccountsOlderThan(cutoffDateTime);

        for (User user : unverifiedAccounts) {
            LocalDateTime createdAt = user.getRegisteredAt();
            if ((!user.getIsEnabled()) && createdAt.isBefore(cutoffDateTime)) {
                // Double-Check if the account has remained unverified for more than 7 days
                    userRepository.delete(user);
            }
        }
    }
}
