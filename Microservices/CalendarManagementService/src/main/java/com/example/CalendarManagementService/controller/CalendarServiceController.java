package com.example.CalendarManagementService.controller;

import com.example.CalendarManagementService.dto.CalendarServiceDTO;
import com.example.CalendarManagementService.entity.CalendarServiceEntity;
import com.example.CalendarManagementService.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/calendars")
public class CalendarServiceController {
    @Autowired
    private CalendarService calendarService;

    @GetMapping
    public List<CalendarServiceDTO> getAllCalendars() {
        return calendarService.getAllCalendars().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{date}/{time}")
    public ResponseEntity<CalendarServiceDTO> getCalendarById(
            @PathVariable LocalDate date, @PathVariable LocalTime time) {
        CalendarServiceEntity calendar = calendarService.getCalendarById(date, time);
        if (calendar != null) {
            return ResponseEntity.ok(convertToDTO(calendar));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public CalendarServiceDTO createCalendar(@RequestBody CalendarServiceDTO calendarDTO) {
        CalendarServiceEntity calendar = calendarService.createCalendar(
                calendarDTO.getDate(), calendarDTO.getTime(), calendarDTO.getTourId());
        return convertToDTO(calendar);
    }

    @DeleteMapping("/{date}/{time}")
    public ResponseEntity<Void> deleteCalendar(
            @PathVariable LocalDate date, @PathVariable LocalTime time) {
        calendarService.deleteCalendar(date, time);
        return ResponseEntity.noContent().build();
    }

    private CalendarServiceDTO convertToDTO(CalendarServiceEntity calendar) {
        return new CalendarServiceDTO(
                calendar.getDate(),
                calendar.getTime(),
                calendar.getTourId()
        );
    }
}
