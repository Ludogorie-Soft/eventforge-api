package com.eventforge.repository;

import com.eventforge.model.OrganisationPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface OrganisationPriorityRepository extends JpaRepository<OrganisationPriority, Long> {
    @Query("SELECT c.category FROM OrganisationPriority c")
    Set<String> getAllOrganisationPriorityCategories();

    OrganisationPriority findByCategory(String category);
}
