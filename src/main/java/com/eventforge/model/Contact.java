package com.eventforge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
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
    private String name;
    private String subject;
    private String text;

    private Boolean isAnswered;
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Contact(String email, String name, String subject, String text) {
        this.email = email;
        this.name = name;
        this.subject = subject;
        this.text = text;
    }
}
