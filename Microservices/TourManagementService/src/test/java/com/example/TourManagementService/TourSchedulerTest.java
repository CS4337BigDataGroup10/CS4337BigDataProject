package com.example.TourManagementService;

import com.example.TourManagementService.entity.Tour;
import com.example.TourManagementService.repository.TourRepository;
import com.example.TourManagementService.service.TourScheduler;
import com.example.TourManagementService.service.TourService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TourSchedulerTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TourRepository tourRepository;

    @InjectMocks
    private TourService tourService;

    @InjectMocks
    private TourScheduler tourScheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDailyTours_Success() {
        when(tourRepository.existsByTourDate(anyString())).thenReturn(false); //making sure no tours exist
        List<Tour> savedTours = new ArrayList<>();
        when(tourRepository.saveAll(anyList())).thenAnswer(invocation -> {
            savedTours.addAll(invocation.getArgument(0));
            return savedTours;
        });

        tourScheduler.createDailyTours();

        assertEquals(9, savedTours.size(), "Incorrect number of daily tours created!");
        verify(tourRepository, times(1)).existsByTourDate(anyString());
        verify(tourRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testCreateDailyTours_AlreadyExists() {
        when(tourRepository.existsByTourDate(anyString())).thenReturn(true); //tour already exists

        tourScheduler.createDailyTours();

        verify(tourRepository, times(1)).existsByTourDate(anyString());
        verify(tourRepository, never()).saveAll(anyList());
    }

    @Test
    void testAssignTourGuideToScheduledTour() {
        when(tourRepository.existsByTourDate(anyString())).thenReturn(false);
        List<Tour> createdTours = new ArrayList<>();
        when(tourRepository.saveAll(anyList())).thenAnswer(invocation -> {
            createdTours.addAll(invocation.getArgument(0));
            return createdTours;
        });

        tourScheduler.createDailyTours();

        Tour scheduledTour = createdTours.get(0);
        int tourId = scheduledTour.getTourId();
        String tourGuideEmail = "guide@example.com";

        when(tourRepository.findById(tourId)).thenReturn(Optional.of(scheduledTour));
        when(tourRepository.save(any(Tour.class))).thenAnswer(invocation -> invocation.getArgument(0));

        tourService.selfAssignToTour(tourId, tourGuideEmail);

        verify(tourRepository, times(1)).findById(tourId);
        verify(tourRepository, times(1)).save(scheduledTour);
        assertEquals(tourGuideEmail, scheduledTour.getEmailId(), "Tour guide assignment failed!");
    }

}
