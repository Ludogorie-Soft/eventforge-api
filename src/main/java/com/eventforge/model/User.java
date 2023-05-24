package com.eventforge.model;

import com.eventforge.enums.Role;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String username;

    private String password;


    private String role;


    @CreationTimestamp
    private LocalDateTime registeredAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    private boolean isEnabled;

    private boolean isLocked;

    public User(String username, String password, boolean isEnabled, boolean isLocked) {
        this.username = username;
        this.password = password;
        this.role = Role.ORGANISATION.toString();
        this.isEnabled = isEnabled;
        this.isLocked = isLocked;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = Role.ADMIN.toString();
        this.isEnabled = true;
        this.isLocked = false;
    }
}
