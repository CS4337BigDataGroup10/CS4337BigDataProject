package com.example.TourManagementService.contoller;

import com.example.TourManagementService.entity.Tour;
import com.example.TourManagementService.service.TourService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tours")
public class TourManagementServiceController {

    private final TourService tourService;

    public TourManagementServiceController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping("/available")
    public ResponseEntity<List<Tour>> getAvailableTours() {
        List<Tour> availableTours = tourService.getAvailableTours();
        return ResponseEntity.ok(availableTours);
    }

    // Get tour details
    @GetMapping("/{tourId}")
    public ResponseEntity<Tour> getTourDetails(@PathVariable int tourId) {
        Tour tour = tourService.getTourById(tourId);
        return ResponseEntity.ok(tour);
    }

    // Update participant count
    @PutMapping("/{tourId}/update-participant-count")
    public ResponseEntity<String> updateParticipantCount(@PathVariable int tourId, @RequestParam int size) {
        try {
            tourService.updateParticipantCount(tourId, size);
            return ResponseEntity.ok("Participant count updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{tourId}/assign-guide")
    public ResponseEntity<String> assignTourGuide(@PathVariable int tourId, @RequestParam String tourGuideId) {
        try {
            tourService.assignTourGuide(tourId, tourGuideId);
            return ResponseEntity.ok("Tour guide assigned successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{tourId}/deassign-guide")
    public ResponseEntity<String> deassignTourGuide(@PathVariable int tourId) {
        try {
            tourService.deassignTourGuide(tourId);
            return ResponseEntity.ok("Tour guide deassigned successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}