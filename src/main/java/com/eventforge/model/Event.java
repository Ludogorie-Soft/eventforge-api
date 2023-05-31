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
    @Size(min = 4, max = 30, message = "Name must be between 4 and 30 characters")
    private String name;
    private String description;
    private String address;
    private List<String> eventCategories;
    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;
    private boolean isOnline;
    @FutureOrPresent(message = "Date must be in the future")
    @CreationTimestamp
    private LocalDateTime createdAt;
    @FutureOrPresent(message = "Date must be in the future")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @FutureOrPresent(message = "Date must be in the future")
    private LocalDateTime startsAt;
    @FutureOrPresent(message = "Date must be in the future")
    private LocalDateTime endsAt;
}
