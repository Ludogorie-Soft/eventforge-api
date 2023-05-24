package com.eventforge.repository;

import com.eventforge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User , UUID> {

    @Query("SELECT a FROM User a WHERE a.role = 'ADMIN'")
    public Optional<User> findAdmin();

    Optional<User> findByUsername(String username);
}
