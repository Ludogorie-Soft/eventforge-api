package com.eventforge.repository;

import com.eventforge.model.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface OrganisationRepository extends JpaRepository<Organisation , UUID> {

    @Query("SELECT o FROM Organisation o WHERE o.user.username = :email")
    Organisation findOrganisationByEmail(@Param("email") String email);

}
