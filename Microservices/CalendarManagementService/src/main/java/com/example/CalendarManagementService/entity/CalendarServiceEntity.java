package com.example.CalendarManagementService.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Calendar")
public class CalendarServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    // Store pairs of time slots and associated tour IDs
    @ElementCollection
    @CollectionTable(name = "TimeSlots", joinColumns = @JoinColumn(name = "CalendarId"))
    private List<TimeSlot> timeSlots = new ArrayList<>();

    public CalendarServiceEntity() {}

    public CalendarServiceEntity(LocalDate date) {
        this.date = date;
        initializeTimeSlots();
    }

    // Initializes hourly slots from 9 AM to 5 PM for each day, with no tourId assigned
    private void initializeTimeSlots() {
        for (int hour = 9; hour <= 17; hour++) {
            timeSlots.add(new TimeSlot(LocalTime.of(hour, 0), null));
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }
}