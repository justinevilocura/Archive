🛠️ Local Setup & API Testing Guide

This guide provides the necessary steps to set up the local database, configure the Spring Boot application, and verify the functionality of all exposed API endpoints.

1. 🗃️ Database Setup (MySQL Workbench)

Execute the following SQL script to create the dbappdevg6jeb database and establish the necessary events, event_links, and audit_log tables with their relationships and indexing.

-- Create and select the database
CREATE DATABASE dbappdevg6jeb;
USE dbappdevg6jeb;
 
-- Drop existing tables (for clean restart/testing)
DROP TABLE IF EXISTS event_links;
DROP TABLE IF EXISTS audit_log;
DROP TABLE IF EXISTS events;
 
-- 1. Events Table (Parent Entity)
CREATE TABLE events (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    department VARCHAR(100) NOT NULL
);
 
-- 2. Event Links Table (Child Entity - One-to-Many Relationship)
CREATE TABLE event_links (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    url VARCHAR(512) NOT NULL,
    platform VARCHAR(50), 
    CONSTRAINT fk_event
        FOREIGN KEY (event_id)
        REFERENCES events(id)
        ON DELETE CASCADE -- Ensure links are deleted if the parent event is deleted
);
 
-- 3. Audit Log Table (Tracking Changes)
CREATE TABLE audit_log (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    action VARCHAR(50) NOT NULL, 
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT NOT NULL,
    user_id VARCHAR(50) NOT NULL DEFAULT 'SYSTEM_OR_ANONYMOUS',
    description TEXT
);
 
-- Indexes for performance
CREATE INDEX idx_event_department ON events (department);
CREATE INDEX idx_audit_entity ON audit_log (entity_type, entity_id);
 
-- Test initial state (should return empty sets)
SELECT * FROM events LIMIT 5;
SELECT * FROM event_links;



2. ⚙️ Application Configuration Check

Verify that the application.properties file has the correct database credentials that match your local MySQL setup.

spring.datasource.url=jdbc:mysql://localhost:3306/dbappdevg6jeb
spring.datasource.username=root
spring.datasource.password=root 
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true

Important: If your MySQL username or password is not root, you must change the corresponding lines in the application.properties file.

3. ▶️ Run the Application

Navigate to the directory containing the pom.xml file (e.g., C:\Users\User\jeb\jeb) and start the Spring Boot application using Maven:
mvn spring-boot:run

4. 🚀 API Endpoint Testing

Once the application is running, use an API client (like Postman, Insomnia, or curl) to test the following REST endpoints, all operating under the base path /api/events.

Event Management Endpoints (CRUD)
POST/api/eventsCreate a new event record.createEvent()
GET/api/eventsRetrieve a list of all events.getAllEvents()
GET/api/events/{id}Retrieve a specific event by its id.getEventById()
PUT/api/events/{id}Update an existing event record by its id.updateEvent()
DELETE/api/events/{id}Delete an event record by its id.deleteEvent()
 
Audit Log Endpoints
GET/api/events/{id}/audit-logRetrieve the audit log history specifically for the event identified by {id}.getEventAuditLogs()
