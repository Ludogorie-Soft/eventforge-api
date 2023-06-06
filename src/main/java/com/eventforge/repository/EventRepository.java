package com.eventforge.repository;

import com.eventforge.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e where e.name=?1")
    Optional<Event> findByName(String name);

    @Query(value = "SELECT e FROM Event e WHERE e.organisation.user.isNonLocked = true AND e.endsAt >= ?1")
    List<Event> findAllValidEvents(LocalDateTime date);

    @Query(value = "SELECT e FROM Event e WHERE e.organisation.user.isNonLocked = true AND e.endsAt < ?1")
    List<Event> findAllValidPassedEvents(LocalDateTime passedDate);

}