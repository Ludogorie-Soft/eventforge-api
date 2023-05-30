package com.eventforge.repository;

import com.eventforge.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken , UUID> {

    public VerificationToken findByToken(String token);
}
