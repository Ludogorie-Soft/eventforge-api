package com.eventforge.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String role;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<Organisation> organisations;
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private VerificationToken verificationToken;
    @OneToMany(mappedBy = "user" , cascade = CascadeType.REMOVE)
    private List<Token> tokens;
    @CreationTimestamp
    private LocalDateTime registeredAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private Boolean isEnabled = false;
    private Boolean isNonLocked = true;
    private Boolean isApprovedByAdmin;

}
