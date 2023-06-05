package com.eventforge.repository;

import com.eventforge.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event , Long> {

    @Query("select e from Event e where e.name=?1")
    Optional<Event> findByName(String name);

    @Query(value = "SELECT e FROM Event e where e.organisation.user.isNonLocked = true ORDER BY e.startsAt {orderBy}", nativeQuery = true)
    List<Event> findAllValidEvents(@Param("orderBy") String orderBy);

}