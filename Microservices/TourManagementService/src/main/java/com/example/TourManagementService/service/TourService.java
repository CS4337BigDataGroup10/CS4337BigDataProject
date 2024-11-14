package com.example.TourManagementService.service;

import com.example.TourManagementService.entity.Tour;
import com.example.TourManagementService.repository.TourRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class TourService {

    private final TourRepository tourRepository;
    private final RestTemplate restTemplate;

    public TourService(TourRepository tourRepository, RestTemplate restTemplate) {
        this.tourRepository = tourRepository;
        this.restTemplate = restTemplate;
    }

    // Method to fetch a tour by ID
    public Tour getTourById(int tourId) {
        return tourRepository.findById(tourId)
                .orElseThrow(() -> new IllegalArgumentException("Tour not found with ID: " + tourId));
    }

    public void selfAssignToTour(int tourId, String emailId) {
        String userManagementServiceUrl = "http://USER-MANAGEMENT-SERVICE/users/" + emailId + "/isTourGuide";
        Boolean isTourGuide = restTemplate.getForObject(userManagementServiceUrl, Boolean.class);

        if (Boolean.FALSE.equals(isTourGuide)) {
            throw new IllegalArgumentException("Only tour guides can assign themselves to a tour.");
        }

        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new IllegalArgumentException("Tour not found with ID: " + tourId));

        if (tour.getEmailId() != null) {
            throw new IllegalArgumentException("Tour already has a tour guide assigned.");
        }

        tour.setEmailId(emailId);
        tourRepository.save(tour);

        System.out.println("Tour guide with EmailID: " + emailId + " assigned to Tour ID: " + tourId);
    }

    public void selfDeassignFromTour(int tourId, String emailId) {
        String userManagementServiceUrl = "http://USER-MANAGEMENT-SERVICE/users/" + emailId + "/isTourGuide";
        Boolean isTourGuide = restTemplate.getForObject(userManagementServiceUrl, Boolean.class);

        if (Boolean.FALSE.equals(isTourGuide)) {
            throw new IllegalArgumentException("Only tour guides can deassign themselves from a tour.");
        }

        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new IllegalArgumentException("Tour not found with ID: " + tourId));

        if (!emailId.equals(tour.getEmailId())) {
            throw new IllegalArgumentException("You are not the assigned tour guide for this tour.");
        }

        tour.setEmailId(null);
        tourRepository.save(tour);

        System.out.println("Tour guide with EmailID: " + emailId + " deassigned from Tour ID: " + tourId);
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