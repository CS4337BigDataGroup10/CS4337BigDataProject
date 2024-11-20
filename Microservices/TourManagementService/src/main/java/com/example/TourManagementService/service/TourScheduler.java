package com.example.TourManagementService.service;

import com.example.TourManagementService.entity.Tour;
import com.example.TourManagementService.repository.TourRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TourScheduler {

    private final TourRepository tourRepository;

    public TourScheduler(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void createDailyTours() {
        // 9am the next day
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0);
        List<Tour> dailyTours = new ArrayList<>();

        // Loop from 9 to 17, creating 9 time slots (9 tours)
        for (int hour = 9; hour <= 17; hour++) {
            LocalDateTime tourDateTime = tomorrow.withHour(hour);

            Tour tour = new Tour();
            tour.setEmailId("default-guide@example.com");
            tour.setTourDateTime(tourDateTime.toString());
            tour.setParticipantCount(0);

            dailyTours.add(tour);
        }
        tourRepository.saveAll(dailyTours);

        System.out.println("Daily tours created for " + tomorrow.toLocalDate());
    }
}