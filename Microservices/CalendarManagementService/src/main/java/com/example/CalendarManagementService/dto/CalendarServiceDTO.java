package com.example.CalendarManagementService.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CalendarServiceDTO {
    private Long id;
    private LocalDate date;
    private List<TimeSlotDTO> timeSlots;

    public CalendarServiceDTO() {}

    public CalendarServiceDTO(Long id, LocalDate date, List<TimeSlotDTO> timeSlots) {
        this.id = id;
        this.date = date;
        this.timeSlots = timeSlots;
    }

    // Inner DTO class for TimeSlot
    public static class TimeSlotDTO {
        private LocalTime timeSlot;
        private Long tourId;

        public TimeSlotDTO() {}

        public TimeSlotDTO(LocalTime timeSlot, Long tourId) {
            this.timeSlot = timeSlot;
            this.tourId = tourId;
        }

        // Getters and setters
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

    // Getters and Setters for CalendarServiceDTO
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<TimeSlotDTO> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlotDTO> timeSlots) {
        this.timeSlots = timeSlots;
    }
}
