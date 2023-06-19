package com.eventforge.repository;

import com.eventforge.model.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, Long> {
    @Query("SELECT o FROM Organisation o WHERE o.user.isApprovedByAdmin = true ORDER BY o.registeredAt ASC")
    List<Organisation> findAllApprovedOrganisationsForAdmin();

    @Query("SELECT o FROM Organisation o WHERE o.user.isApprovedByAdmin = false ORDER BY o.registeredAt ASC")
    List<Organisation> findAllUnapprovedOrganisationsForAdmin();

    @Query("SELECT o FROM Organisation o WHERE o.user.username = :email")
    Organisation findOrganisationByEmail(@Param("email") String email);

    Optional<Organisation> findOrganisationByName(String name);

    @Query("SELECT o FROM Organisation o WHERE o.user.id = :userId")
    Organisation findOrganisationByUserId(Long userId);

    @Query("SELECT o FROM Organisation o WHERE o.user.isEnabled = true AND o.user.isApprovedByAdmin = true AND o.user.isNonLocked = true")
    List<Organisation> findAllOrganisations();

    @Query("SELECT o FROM Organisation o WHERE o.user.isEnabled = true AND o.user.isApprovedByAdmin = true AND o.user.isNonLocked = true AND o.name LIKE %:name%")
    List<Organisation> findAllOrganisationsForUserByName(@RequestParam("name") String name);
}
