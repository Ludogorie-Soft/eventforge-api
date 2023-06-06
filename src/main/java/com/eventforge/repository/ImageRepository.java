package com.eventforge.repository;

import com.eventforge.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("select i from Image i where i.url=?1")
    Optional<Image> findByUrl(String fileName);
}
