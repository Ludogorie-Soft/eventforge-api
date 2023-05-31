package com.eventforge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organisation {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;

    private String bullstat;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    private String address;

    @ManyToMany
    @JoinTable(
            name = "priorityId_organisationId",
            joinColumns = @JoinColumn(name = "priority_id"),
            inverseJoinColumns = @JoinColumn(name = "organisation_id")
    )
    private Set<OrganisationPriority> organisationPriorities;

    private String website;

    private String facebookLink;

    private String charityOption;

    private String purposeOfOrganisation;

    @CreationTimestamp
    private LocalDateTime registeredAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
