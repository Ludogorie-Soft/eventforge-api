package com.eventforge.repository;

import com.eventforge.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken , Long> {

    Optional<VerificationToken> findByToken(String token);

}
