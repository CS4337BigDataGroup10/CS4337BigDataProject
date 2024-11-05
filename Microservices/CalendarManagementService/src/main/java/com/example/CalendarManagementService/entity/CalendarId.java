package com.example.CalendarManagementService.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Embeddable
public class CalendarId implements Serializable {

    private LocalDate date;
    private LocalTime time;

    public CalendarId() {}

    public CalendarId(LocalDate date, LocalTime time) {
        this.date = date;
        this.time = time;
    }

    // Getters, setters, equals, and hashCode
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarId that = (CalendarId) o;
        return date.equals(that.date) && time.equals(that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time);
    }
}
