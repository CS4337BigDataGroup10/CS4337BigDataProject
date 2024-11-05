package com.example.CalendarManagementService.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class CalendarServiceDTO {
    private LocalDate date;
    private LocalTime time;
    private Long tourId;

    public CalendarServiceDTO() {}

    public CalendarServiceDTO(LocalDate date, LocalTime time, Long tourId) {
        this.date = date;
        this.time = time;
        this.tourId = tourId;
    }

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

    public Long getTourId() {
        return tourId;
    }

    public void setTourId(Long tourId) {
        this.tourId = tourId;
    }
}
