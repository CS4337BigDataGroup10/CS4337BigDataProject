package com.example.TourManagementService.contoller;

import com.example.TourManagementService.dto.BookingNotificationDTO;
import com.example.TourManagementService.entity.Tour;
import com.example.TourManagementService.service.TourService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tours")
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

    @PostMapping("/{tourId}/self-assign")
    public void selfAssignToTour(@PathVariable int tourId, @RequestParam String emailId) {
        tourService.selfAssignToTour(tourId, emailId);
    }

    @PostMapping("/{tourId}/self-deassign")
    public void selfDeassignFromTour(@PathVariable int tourId, @RequestParam String emailId) {
        tourService.selfDeassignFromTour(tourId, emailId);
    }
    @PostMapping("/create")
    public ResponseEntity<Tour> createTour(@RequestBody Tour tour) {
        Tour savedTour = tourService.createTour(tour);
        return ResponseEntity.ok(savedTour);
    }

    @PutMapping("/{tourId}/addBooking")
    public ResponseEntity<String> addBooking(BookingNotificationDTO bookingNotificationDto) {
        try {
            tourService.addBookingtoTour(bookingNotificationDto);
            return ResponseEntity.ok("Booking added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{tourId}/removeBooking")
    public ResponseEntity<String> removeBooking(BookingNotificationDTO bookingNotificationDto) {
        try {
            tourService.removeBooking(bookingNotificationDto.getTourId(), bookingNotificationDto.getBookingId());
            return ResponseEntity.ok("Booking removed successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}