package com.eventforge.repository;

import com.eventforge.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("select i from Image i where i.url=?1")
    Optional<Image> findByUrl(String fileName);
    @Query("SELECT i FROM Image i WHERE i.url = :imageUrl")
    Optional<Image> findImageByUrl(String imageUrl);

    @Query("SELECT i FROM Image i WHERE i.organisation.id = :id AND i.type = 'LOGO' ")
     Image findOrganisationLogoByOrgId(Long id);

    @Query("SELECT i FROM Image i WHERE i.organisation.id = :id AND i.type = 'COVER'")
     Image findOrganisationCoverPictureByOrgId(Long id);

    @Query("SELECT i FROM Image i WHERE i.event.id = :id AND i.type = 'EVENT_PICTURE' AND i.id = :imageId")
    Image findEventPictureByEventIdImage(Long id , Long imageId);
}
