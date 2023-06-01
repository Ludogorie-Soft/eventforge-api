package com.eventforge.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    @Size(min = 4, max = 30, message = "Името на събитието трява да е между 4 и 30 символа!")
    private String name;
    private String description;
    private String address;
    private List<String> eventCategories;
    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;
    private boolean isOnline;
    @FutureOrPresent(message = "Трябва да въведете текуща или дата!")
    @CreationTimestamp
    private LocalDateTime createdAt;
    @FutureOrPresent(message = "Трябва да въведете текуща или дата!")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @FutureOrPresent(message = "Трябва да въведете текуща или дата!")
    private LocalDateTime startsAt;
    @FutureOrPresent(message = "Трябва да въведете текуща или дата!")
    private LocalDateTime endsAt;
}
