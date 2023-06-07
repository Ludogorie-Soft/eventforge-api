package com.eventforge.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.Set;
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Organisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private String bullstat;
    @ManyToOne(cascade ={CascadeType.MERGE, CascadeType.REMOVE} )
    @JoinColumn(name = "user_id")
    private User user;

    private String address;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "priority_id_organisation_id",
            joinColumns = @JoinColumn(name = "organisation_id"),
            inverseJoinColumns = @JoinColumn(name = "organisation_priority_id")
    )
    private Set<OrganisationPriority> organisationPriorities;

    private String website;

    private String facebookLink;

    private String charityOption;

    private String organisationPurpose;

    @CreationTimestamp
    private LocalDateTime registeredAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
