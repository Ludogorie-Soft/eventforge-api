package com.eventforge.repository;

import com.eventforge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT a FROM User a WHERE a.role = 'ADMIN' AND a.username = :username")
    Optional<User> findAdmin(String username);

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByEmail(String username);

    @Query("SELECT u.isEnabled FROM User u WHERE u.username = :username")
    boolean isAccountVerified(String username);

    @Query("SELECT u FROM User u WHERE u.isEnabled = false AND u.registeredAt < :cutoffDateTime")
    List<User> getUnverifiedAccountsOlderThan(LocalDateTime cutoffDateTime);
}
