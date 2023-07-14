package com.eventforge.service;

import com.eventforge.exception.InvalidEmailConfirmationLinkException;
import com.eventforge.model.VerificationToken;
import com.eventforge.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationToken getVerificationTokenByToken(String token){
        return verificationTokenRepository.findByToken(token).orElseThrow(() -> new InvalidEmailConfirmationLinkException("Линкът за активация е навалиден"));
    }

    public boolean isVerificationTokenExpired(String verificationToken){
        Calendar calendar = Calendar.getInstance();
        return getVerificationTokenByToken(verificationToken).getExpirationTime().getTime() - calendar.getTime().getTime() <= 0;
    }

    public String validateVerificationToken(String verificationToken, String url) {
        boolean isExpired = isVerificationTokenExpired(verificationToken);
        if(isExpired){
            return url;
        }
        return null;
    }

    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = getVerificationTokenByToken(oldToken);
        var verificationTokenTime = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpirationTime(verificationTokenTime.getTokenExpirationTime());
        saveVerificationToken(verificationToken);
        return verificationToken;
    }
    public void deleteVerificationToken (VerificationToken token){
        verificationTokenRepository.delete(token);
    }

    public void saveVerificationToken(VerificationToken token){
        verificationTokenRepository.save(token);
    }


}
