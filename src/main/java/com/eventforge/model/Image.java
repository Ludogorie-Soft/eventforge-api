package com.eventforge.model;

import com.eventforge.enums.ImageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String url;

    @CreationTimestamp
    private LocalDateTime uploadAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;

    private String type;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event evenId;
    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisationId;


}
