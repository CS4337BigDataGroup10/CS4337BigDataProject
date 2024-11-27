package com.example.BookingMakingService.contoller;

import com.example.BookingMakingService.entity.Booking;
import com.example.BookingMakingService.entity.Tour;
import com.example.BookingMakingService.service.BookingService;
import com.example.BookingMakingService.service.TourManagementClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookingController {

    private final BookingService bookingService;
    private final TourManagementClient tourManagementClient;

    public BookingController(BookingService bookingService, TourManagementClient tourManagementClient) {
        this.bookingService = bookingService;
        this.tourManagementClient = tourManagementClient;
    }

    @GetMapping("/bookings")
    public ResponseEntity<String> getAllBookings() {
        String currentBookings = bookingService.getAllBookings();
        return ResponseEntity.ok(currentBookings);
    }

    // New endpoint to get bookings by emailId
    @GetMapping("/email/{emailId}")
    public ResponseEntity<String> getBookingsByEmailId(@PathVariable String emailId) {
        String userBookings = bookingService.getBookingsByEmailId(emailId).toString();
        if (userBookings.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userBookings);
    }

    @PostMapping
    public ResponseEntity<String> createBooking(@RequestBody Booking booking) {
        // Step 1: Fetch all available tours
        List<Tour> availableTours = tourManagementClient.getNonFullTours();

        // Step 2: Check if the requested tour is available
        boolean tourAvailable = availableTours.stream()
                .anyMatch(tour -> tour.getTourId() == booking.getTourId());
                //this is checking if the tour we are trying to book into is available and there. If it is not available, it will return a bad request
        if (!tourAvailable) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Selected tour is not available.");
        }

        // Step 3: Proceed with booking creation
        //Adds to db of booking repository
        Booking newBooking = bookingService.createBooking(booking);
        tourManagementClient.notifyTourManagement(newBooking); //this notifys tour management service that the booking has been created after going through the checks.
        return ResponseEntity.ok("Booking created successfully and notification sent.");
    }

    // New endpoint to cancel a booking
    @PutMapping("/bookings/{bookingId}/cancel")
    public ResponseEntity<String> cancelBooking(@PathVariable Booking booking) {
        boolean isCancelled = bookingService.cancelBooking(booking.getBookingId());

        if (isCancelled) {
            tourManagementClient.notifyCancellation(booking);
            return ResponseEntity.ok("Booking with ID " + booking.getBookingId() + " has been cancelled.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Booking with ID " + booking.getBookingId() + " is already cancelled or does not exist.");
        }
    }



}
