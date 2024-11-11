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

    // For admin
    public void assignTourGuide(int tourId, String tourGuideId){
        Tour tour = getTourById(tourId);
        tour.setTourGuideId(tourGuideId);
        tourRepository.save(tour);
    }
    public void deassignTourGuide(int tourId) {
        Tour tour = getTourById(tourId);
        tour.setTourGuideId(null);
        tourRepository.save(tour);
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
    public void removeBooking(int tourId, int bookingId) {
        @Query("DELETE t FROM Tour t WHERE t.bookingId = bookingId")
        Tour tour = getTourById(tourId);
        tour.updateParticipantCount(-1);
    }

    public void addBooking(int tourId, int bookingId) {
        @Query("INSERT INTO Tour t WHERE t.bookingId = bookingId")
        Tour tour = getTourById(tourId);
        tour.updateParticipantCount(1);
    }

}