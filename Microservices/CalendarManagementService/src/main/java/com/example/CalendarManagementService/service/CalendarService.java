package com.example.CalendarManagementService.service;

import com.example.CalendarManagementService.entity.CalendarServiceEntity;
import com.example.CalendarManagementService.entity.CalendarId;
import com.example.CalendarManagementService.repository.CalendarServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class CalendarService {
    @Autowired
    private CalendarServiceRepository calendarServiceRepository;

    public List<CalendarServiceEntity> getAllCalendars() {
        return calendarServiceRepository.findAll();
    }

    public CalendarServiceEntity getCalendarById(LocalDate date, LocalTime time) {
        return calendarServiceRepository.findById(new CalendarId(date, time)).orElse(null);
    }

    public CalendarServiceEntity createCalendar(LocalDate date, LocalTime time, Long tourId) {
        CalendarServiceEntity calendar = new CalendarServiceEntity(date, time, tourId);
        return calendarServiceRepository.save(calendar);
    }

    public void deleteCalendar(LocalDate date, LocalTime time) {
        calendarServiceRepository.deleteById(new CalendarId(date, time));
    }
}
