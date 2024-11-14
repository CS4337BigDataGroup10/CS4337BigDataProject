package com.example.TourManagementService.service;

import com.example.TourManagementService.entity.Tour;
import com.example.TourManagementService.repository.TourRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TourService {

    private final TourRepository tourRepository;

    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    public List<Tour> getAvailableTours() {
        return tourRepository.findAvailableTours();
    }

    // Method to fetch a tour by ID
    public Tour getTourById(int tourId) {
        return tourRepository.findById(tourId)
                .orElseThrow(() -> new IllegalArgumentException("Tour not found with ID: " + tourId));
    }

    public void selfAssignToTour(int tourId, String emailId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new IllegalArgumentException("Tour not found with ID: " + tourId));

        tour.setEmailId(emailId);
        tourRepository.save(tour);

        System.out.println("User with EmailID: " + emailId + " assigned to Tour ID: " + tourId);
    }

    public void selfDeassignFromTour(int tourId, String emailId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new IllegalArgumentException("Tour not found with ID: " + tourId));

        if (!emailId.equals(tour.getEmailId())) {
            throw new IllegalArgumentException("You are not assigned to this tour or already deassigned.");
        }
        tour.setEmailId(null);  // Clear the assignment
        tourRepository.save(tour);

        System.out.println("User with EmailID: " + emailId + " deassigned from Tour ID: " + tourId);
    }

    // Method to update participant count in a tour
    @Transactional
    public void updateParticipantCount(int tourId, int size) {
        Tour tour = getTourById(tourId);
        int newParticipantCount = tour.getParticipantCount() + size;

        // Check if the new count exceeds the maximum capacity
        if (newParticipantCount > 20) {
            throw new IllegalArgumentException("Cannot add booking, tour capacity exceeded.");
        }

        tour.setParticipantCount(newParticipantCount);
        tourRepository.save(tour);
    }
    public Tour createTour(Tour tour) {
        return tourRepository.save(tour);
    }
}