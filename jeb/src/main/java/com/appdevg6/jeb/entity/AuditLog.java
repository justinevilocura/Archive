package com.appdevg6.jeb.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Tracks all significant actions within the system for accountability.
 */
@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Instant timestamp = Instant.now();

    // The action performed (e.g., 'EVENT_CREATED', 'EVENT_UPDATED', 'EVENT_DELETED')
    @Column(nullable = false)
    private String action; 

    // Entity type being acted upon (e.g., 'Event', 'PermissionRequest')
    @Column(nullable = false)
    private String entityType; 

    // ID of the entity that was acted upon
    @Column(nullable = false)
    private Long entityId; 

    // Placeholder for user ID; will be used when User entity is introduced
    @Column(name = "user_id")
    private String userId = "SYSTEM_OR_ANONYMOUS"; 
    
    // Detailed description of the action
    @Column(columnDefinition = "TEXT")
    private String description; 

    // --- Constructors, Getters, and Setters ---

    public AuditLog() {}

    // Convenience constructor for creating a log entry
    public AuditLog(String action, String entityType, Long entityId, String description) {
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}