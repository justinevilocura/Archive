package com.appdevg6.jeb.service;

import com.appdevg6.jeb.entity.AuditLog;
import com.appdevg6.jeb.entity.Event;
import com.appdevg6.jeb.entity.EventLink;
import com.appdevg6.jeb.repository.AuditLogRepository;
import com.appdevg6.jeb.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final AuditLogRepository auditLogRepository;

    @Autowired
    public EventService(EventRepository eventRepository, AuditLogRepository auditLogRepository) {
        this.eventRepository = eventRepository;
        this.auditLogRepository = auditLogRepository;
    }

    /**
     * Creates a new Event and logs the creation in the AuditLog.
     */
    @Transactional
    public Event createEvent(Event event) {
        // Ensure bi-directional link relationship before saving
        if (event.getLinks() != null) {
            event.getLinks().forEach(link -> link.setEvent(event));
        }
        
        Event savedEvent = eventRepository.save(event);

        // 1. Log the creation
        AuditLog log = new AuditLog(
            "EVENT_CREATED",
            Event.class.getSimpleName(),
            savedEvent.getId(),
            "New event '" + savedEvent.getTitle() + "' created."
        );
        auditLogRepository.save(log);

        return savedEvent;
    }

    /**
     * Updates an existing Event and logs the modification in the AuditLog.
     */
    @Transactional
    public Optional<Event> updateEvent(Long id, Event eventDetails) {
        return eventRepository.findById(id).map(existingEvent -> {
            
            String oldTitle = existingEvent.getTitle();
            String changes = "";

            // Update core fields
            if (!existingEvent.getTitle().equals(eventDetails.getTitle())) {
                changes += "Title changed from '" + oldTitle + "' to '" + eventDetails.getTitle() + "'. ";
                existingEvent.setTitle(eventDetails.getTitle());
            }
            if (!existingEvent.getDate().isEqual(eventDetails.getDate())) {
                changes += "Date updated. ";
                existingEvent.setDate(eventDetails.getDate());
            }
            if (!existingEvent.getDepartment().equals(eventDetails.getDepartment())) {
                changes += "Department updated. ";
                existingEvent.setDepartment(eventDetails.getDepartment());
            }

            // Update links: Clear old links and add new ones (handled by orphanRemoval=true)
            existingEvent.getLinks().clear();
            if (eventDetails.getLinks() != null) {
                 eventDetails.getLinks().forEach(link -> existingEvent.addLink(link));
                 changes += eventDetails.getLinks().size() + " links replaced/updated. ";
            } else {
                 changes += "All links removed. ";
            }
            
            Event updatedEvent = eventRepository.save(existingEvent);

            // 2. Log the update
            if (!changes.isEmpty()) {
                 AuditLog log = new AuditLog(
                    "EVENT_UPDATED",
                    Event.class.getSimpleName(),
                    updatedEvent.getId(),
                    "Event '" + updatedEvent.getTitle() + "' updated. Changes: " + changes.trim()
                );
                auditLogRepository.save(log);
            }

            return updatedEvent;
        });
    }

    /**
     * Deletes an Event by ID and logs the action.
     */
    @Transactional
    public boolean deleteEvent(Long id) {
        if (eventRepository.existsById(id)) {
            // Get the title before deletion for the log
            String eventTitle = eventRepository.findById(id).orElseThrow().getTitle(); 
            eventRepository.deleteById(id);

            // 3. Log the deletion
            AuditLog log = new AuditLog(
                "EVENT_DELETED",
                Event.class.getSimpleName(),
                id, // Log the ID that was deleted
                "Event '" + eventTitle + "' deleted."
            );
            auditLogRepository.save(log);
            return true;
        }
        return false;
    }

    // --- Read/Retrieve Methods ---
    
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
    
    public List<AuditLog> getEventAuditLogs(Long eventId) {
        return auditLogRepository.findByEntityTypeAndEntityIdOrderByTimestampDesc(
            Event.class.getSimpleName(), eventId);
    }
}