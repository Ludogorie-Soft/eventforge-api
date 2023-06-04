package com.eventforge.repository;

import com.eventforge.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event , Long> {

    @Query("select e from Event e where e.name=?1")
    Optional<Event> findByName(String name);
}
