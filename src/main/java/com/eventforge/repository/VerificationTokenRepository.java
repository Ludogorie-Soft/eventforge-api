package com.eventforge.repository;

import com.eventforge.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken , Long> {

    VerificationToken findByToken(String token);
    VerificationToken deleteByUserId(Long id);
}
