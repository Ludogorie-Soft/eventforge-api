package com.eventforge.repository;

import com.eventforge.model.OrganisationPriority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrganisationPriorityRepository extends JpaRepository<OrganisationPriority , UUID> {
    OrganisationPriority findByCategory(String category);
}
