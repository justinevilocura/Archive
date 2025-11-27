First Set up the SQL Workbench:
CREATE DATABASE dbappdevg6jeb;
USE dbappdevg6jeb;
 
DROP TABLE IF EXISTS event_links;
DROP TABLE IF EXISTS audit_log;
DROP TABLE IF EXISTS events;
 
-- 1. Events Table
CREATE TABLE events (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- MySQL syntax for auto-increment
    title VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    department VARCHAR(100) NOT NULL
    -- Future: user_id BIGINT REFERENCES users(id)
);
 
-- 2. Event Links Table
CREATE TABLE event_links (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- MySQL syntax for auto-increment
    event_id BIGINT NOT NULL,
    url VARCHAR(512) NOT NULL,
    platform VARCHAR(50), -- e.g., 'YouTube', 'Flickr'
    CONSTRAINT fk_event
        FOREIGN KEY (event_id)
        REFERENCES events(id)
        ON DELETE CASCADE -- If the event is deleted, its links are also deleted
);
 
-- 3. Audit Log Table
CREATE TABLE audit_log (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- MySQL syntax for auto-increment
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Changed TIMESTAMP WITH TIME ZONE to plain TIMESTAMP for MySQL
    action VARCHAR(50) NOT NULL,        -- e.g., 'EVENT_CREATED', 'EVENT_UPDATED', 'EVENT_DELETED'
    entity_type VARCHAR(50) NOT NULL,   -- e.g., 'Event'
    entity_id BIGINT NOT NULL,          -- ID of the entity that was modified
    user_id VARCHAR(50) NOT NULL DEFAULT 'SYSTEM_OR_ANONYMOUS',
    description TEXT
);
 
-- Indexes for performance
CREATE INDEX idx_event_department ON events (department);
CREATE INDEX idx_audit_entity ON audit_log (entity_type, entity_id);
 
SELECT * FROM events LIMIT 5;
SELECT * FROM event_links;

After that ensure your application properties username or password is the same as the sql password and username:
spring.datasource.url=jdbc:mysql://localhost:3306/dbappdevg6jeb
spring.datasource.username=root
spring.datasource.password=root //if your password is diffent than mine then change it
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true


Next is run the application to see if theres an error:
mvn spring-boot:run
ensure to run it in the jeb/jeb folder where the pom.xml is located.
ex: cd C:\Users\User\jeb\jeb


Then run the endpoints:
Event Management Endpoints (CRUD)
POST/api/eventsCreate a new event record.createEvent()
GET/api/eventsRetrieve a list of all events.getAllEvents()
GET/api/events/{id}Retrieve a specific event by its id.getEventById()
PUT/api/events/{id}Update an existing event record by its id.updateEvent()
DELETE/api/events/{id}Delete an event record by its id.deleteEvent()
 
Audit Log Endpoints
GET/api/events/{id}/audit-logRetrieve the audit log history specifically for the event identified by {id}.getEventAuditLogs()
