package com.appdevg6.jeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appdevg6.jeb.entity.EventLink;

/**
 * Repository for EventLink entity (mostly managed via Event).
 */
@Repository
public interface EventLinkRepository extends JpaRepository<EventLink, Long> {
    // No additional methods needed for now.
}