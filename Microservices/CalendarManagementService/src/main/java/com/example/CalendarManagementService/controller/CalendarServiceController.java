package com.example.CalendarManagementService.controller;

import com.example.CalendarManagementService.dto.CalendarServiceDTO;
import com.example.CalendarManagementService.entity.CalendarServiceEntity;
import com.example.CalendarManagementService.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<CalendarServiceDTO> getCalendarById(@PathVariable Long id) {
        CalendarServiceEntity calendar = calendarService.getCalendarById(id);
        if (calendar != null) {
            return ResponseEntity.ok(convertToDTO(calendar));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public CalendarServiceDTO createCalendar(@RequestBody CalendarServiceDTO calendarDTO) {
        CalendarServiceEntity calendar = calendarService.createCalendar(calendarDTO.getDate());
        return convertToDTO(calendar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalendar(@PathVariable Long id) {
        calendarService.deleteCalendar(id);
        return ResponseEntity.noContent().build();
    }

    private CalendarServiceDTO convertToDTO(CalendarServiceEntity calendar) {
        List<CalendarServiceDTO.TimeSlotDTO> timeSlotDTOs = calendar.getTimeSlots().stream()
                .map(timeSlot -> new CalendarServiceDTO.TimeSlotDTO(timeSlot.getTimeSlot(), timeSlot.getTourId()))
                .collect(Collectors.toList());
        return new CalendarServiceDTO(calendar.getId(), calendar.getDate(), timeSlotDTOs);
    }
}

