package com.eventforge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @CreationTimestamp
    private LocalDateTime uploadAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;

    private String type;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event even;
    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

}
