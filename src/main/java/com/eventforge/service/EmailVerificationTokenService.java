package com.eventforge.service;

import com.eventforge.constants.TokenType;
import com.eventforge.exception.InvalidEmailConfirmationLinkException;
import com.eventforge.model.VerificationToken;
import com.eventforge.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class EmailVerificationTokenService {

    private static final int EXPIRATION_TIME = 3; //days

    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationToken getVerificationTokenByToken(String token){
        return verificationTokenRepository.findByToken(token).orElseThrow(() -> new InvalidEmailConfirmationLinkException("Линкът вече е невалиден!"));
    }

    public void validateTokenExpirationTime(VerificationToken token){
        LocalDateTime updatedAt = token.getUpdatedAt();
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime expirationTime = updatedAt.plus(EXPIRATION_TIME, ChronoUnit.DAYS);
        if(now.isAfter(expirationTime)){
            String errorMessage = (token.getType().equals(TokenType.CONFIRM_EMAIL.toString()))
                    ? "Линкът за потвърждение на акаунт е изтекъл. Моля, генерирайте нов."
                    : "Линкът за забравена парола е изтекъл. Моля, генерирайте нов.";

            throw new InvalidEmailConfirmationLinkException(errorMessage);
        }
    }

    public void deleteVerificationToken (VerificationToken token){
        verificationTokenRepository.delete(token);
    }

    public void saveVerificationToken(VerificationToken token){
        verificationTokenRepository.save(token);
    }


}
