package com.example.CalendarManagementService.entity;

import jakarta.persistence.Embeddable;
import java.time.LocalTime;

@Embeddable
public class TimeSlot {
    private LocalTime timeSlot;
    private Long tourId;

    public TimeSlot() {}

    public TimeSlot(LocalTime timeSlot, Long tourId) {
        this.timeSlot = timeSlot;
        this.tourId = tourId;
    }

    // Getters and Setters
    public LocalTime getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(LocalTime timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Long getTourId() {
        return tourId;
    }

    public void setTourId(Long tourId) {
        this.tourId = tourId;
    }
}