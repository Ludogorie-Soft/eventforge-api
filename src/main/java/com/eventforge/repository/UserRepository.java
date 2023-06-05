package com.eventforge.repository;

import com.eventforge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User , Long> {

    @Query("SELECT a FROM User a WHERE a.role = 'ADMIN'")
     Optional<User> findAdmin();
    Optional<User> findByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByEmail(String username);
    @Query("SELECT u.isEnabled FROM User u WHERE u.username = :username")
    boolean isAccountVerified(String username);


}
