package com.eventforge.model;

import com.eventforge.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
    @Size(min = 4, max = 30, message = "Името на събитието трява да е между 4 и 30 символа!")
    @Column(unique = true)
    private String username;
    @Column(length = 64, nullable = false)
    private String password;
    private String name;
    private String phone;
    private String role;
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;
    @CreationTimestamp
    private LocalDateTime registeredAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private boolean isEnabled;
    private boolean isNonLocked;

    public User(String username, String password, boolean isEnabled, boolean isNonLocked) {
        this.username = username;
        this.password = password;
        this.role = Role.ORGANISATION.toString();
        this.isEnabled = isEnabled;
        this.isNonLocked = isNonLocked;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = Role.ADMIN.toString();
        this.isEnabled = true;
        this.isNonLocked = false;
    }
}
