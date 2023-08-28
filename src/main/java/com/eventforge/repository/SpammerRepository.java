package com.eventforge.repository;

import com.eventforge.model.Spammer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpammerRepository extends JpaRepository<Spammer , Long> {
    Optional<Spammer> findByEmail(String email);
}
