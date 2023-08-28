package com.eventforge.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String subject;
    private String text;

    private Boolean isAnswered;
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Contact(String email, String subject, String text) {
        this.email = email;
        this.subject = subject;
        this.text = text;
    }
}
