package com.eventforge.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
//    @JoinTable(name = "eventId_enrollmentId")
    @JoinTable(name = "event_enrollment_event",
            joinColumns = @JoinColumn(name = "event_enrollment_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> eventId;
    @Nullable
    private String phone;
    @Nullable
    private String externalLink;
    @Nullable
    private String email;
}
