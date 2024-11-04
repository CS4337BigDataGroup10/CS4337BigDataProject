package com.example.CalendarManagementService.service;

import com.example.CalendarManagementService.entity.CalendarServiceEntity;
import com.example.CalendarManagementService.repository.CalendarServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CalendarService {
    @Autowired
    private CalendarServiceRepository calendarServiceRepository;

    public List<CalendarServiceEntity> getAllCalendars() {
        return calendarServiceRepository.findAll();
    }

    public CalendarServiceEntity getCalendarById(Long id) {
        return calendarServiceRepository.findById(id).orElse(null);
    }

    public CalendarServiceEntity createCalendar(LocalDate date) {
        CalendarServiceEntity calendar = new CalendarServiceEntity(date);
        return calendarServiceRepository.save(calendar);
    }

    public void deleteCalendar(Long id) {
        calendarServiceRepository.deleteById(id);
    }


}