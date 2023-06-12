package com.eventforge.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String address;
    private String eventCategories;
    private double price;
    private Integer minAge;
    private Integer maxAge;
    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;
    private Boolean isOnline;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private Boolean isOneTime;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;

    //for recurrence events
    private String recurrenceDetails;
}
