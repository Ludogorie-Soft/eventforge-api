package com.eventforge.repository;

import com.eventforge.model.EventEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventEnrollmentRepository extends JpaRepository<EventEnrollment, Long> {
}
