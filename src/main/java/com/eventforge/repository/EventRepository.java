package com.eventforge.repository;

import com.eventforge.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event , UUID> {
}
