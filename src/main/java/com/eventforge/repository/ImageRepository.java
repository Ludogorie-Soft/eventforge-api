package com.eventforge.repository;

import com.eventforge.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image , Long> {
}
