package com.example.CalendarManagementService.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Calendar")  public class CalendarServiceEntity {

    @EmbeddedId
    private CalendarId calendarId;

    @Column(name = "tourId")
    private Long tourId;

    public CalendarServiceEntity() {}

    public CalendarServiceEntity(LocalDate date, LocalTime time, Long tourId) {
        this.calendarId = new CalendarId(date, time);
        this.tourId = tourId;
    }

    // Getters and Setters
    public LocalDate getDate() {
        return calendarId.getDate();
    }

    public LocalTime getTime() {
        return calendarId.getTime();
    }

    public Long getTourId() {
        return tourId;
    }

    public void setTourId(Long tourId) {
        this.tourId = tourId;
    }
}
