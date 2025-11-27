package com.appdevg6.jeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appdevg6.jeb.entity.Event;

/**
 * Repository for Event entity.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // Custom query methods can be added here if needed, 
    // e.g., List<Event> findByDepartment(String department);
}