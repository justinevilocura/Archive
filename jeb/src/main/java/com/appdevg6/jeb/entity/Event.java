package com.appdevg6.jeb.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a past marketing event.
 */
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate date;

    // Simplified for now; would typically be a Many-to-One to a Department entity
    @Column(nullable = false)
    private String department; 

    // One-to-Many relationship with EventLink. 
    // Cascade ALL ensures links are saved/deleted with the event.
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventLink> links = new ArrayList<>();

    // --- Constructors, Getters, and Setters ---

    public Event() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<EventLink> getLinks() {
        return links;
    }

    public void setLinks(List<EventLink> links) {
        // Ensure bidirectional relationship is maintained when setting links
        this.links.clear();
        if (links != null) {
            links.forEach(this::addLink);
        }
    }

    public void addLink(EventLink link) {
        links.add(link);
        link.setEvent(this);
    }
}