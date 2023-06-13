package com.eventforge.model;

import com.eventforge.constants.ImageType;
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
    @Enumerated(EnumType.STRING)
    private ImageType type;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

}
