package com.eventforge.repository;

import com.eventforge.model.EventEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventEnrollmentRepository extends JpaRepository<EventEnrollment , UUID> {
}
