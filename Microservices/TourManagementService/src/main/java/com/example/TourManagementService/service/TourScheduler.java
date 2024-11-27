package com.example.TourManagementService.service;

import com.example.TourManagementService.entity.Tour;
import com.example.TourManagementService.repository.TourRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TourScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TourScheduler.class);
    private static final int START_HOUR = 9;
    private static final int END_HOUR = 17;

    private final TourRepository tourRepository;

    public TourScheduler(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void createDailyTours() {
        // 9am the next day
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0);

        // Check if tours for tomorrow are already created
        if (tourRepository.existsByTourDate(String.valueOf(tomorrow.toLocalDate()))) {
            logger.info("Tours for {} already exist.", tomorrow.toLocalDate());
            return;
        }

        List<Tour> dailyTours = new ArrayList<>();
        for (int hour = START_HOUR; hour <= END_HOUR; hour++) {
            LocalDateTime tourDateTime = tomorrow.withHour(hour);

            Tour tour = new Tour();
            tour.setEmailId(null); // No guide assigned yet
            tour.setTourDateTime(tourDateTime.toString());
            tour.setParticipantCount(0);
            tour.setMaxCapacity(20); // Default capacity

            dailyTours.add(tour);
        }

        tourRepository.saveAll(dailyTours);
        logger.info("Daily tours created for {}", tomorrow.toLocalDate());
    }
}
