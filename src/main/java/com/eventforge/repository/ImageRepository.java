package com.eventforge.repository;

import com.eventforge.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("select i from Image i where i.url=?1")
    Optional<Image> findByUrl(String fileName);
    @Query("SELECT i FROM Image i WHERE i.url = :fileName")
    Optional<Image> findImageByName(String fileName);

    @Query("SELECT i.url FROM Image i WHERE i.organisation.id = :id AND i.type = 'LOGO' ")
     String findOrganisationLogoByOrgId(Long id);

    @Query("SELECT i.url FROM Image i WHERE i.organisation.id = :id AND i.type = 'COVER'")
     String findOrganisationCoverPictureByOrgId(Long id);
}
