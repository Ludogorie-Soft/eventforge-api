package com.eventforge.service;

import com.eventforge.model.VerificationToken;
import com.eventforge.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationToken getVerificationTokenByToken(String token){
        return verificationTokenRepository.findByToken(token);
    }

    public void deleteVerificationToken (VerificationToken token){
        verificationTokenRepository.delete(token);
    }

    public void saveVerificationToken(VerificationToken token){
        verificationTokenRepository.save(token);
    }


}
