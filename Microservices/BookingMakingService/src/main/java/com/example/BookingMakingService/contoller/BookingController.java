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
        try {
            // Step 1: Fetch all available tours
            List<Tour> availableTours = tourManagementClient.getNonFullTours();

            // Step 2: Validate if the requested tour is available
            boolean tourAvailable = availableTours.stream()
                    .anyMatch(tour -> tour.getTourId() == booking.getTourId());

            if (!tourAvailable) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Selected tour is not available or is fully booked.");
            }

            // Step 3: Create the booking locally
            Booking newBooking = bookingService.createBooking(booking);

            // Step 4: Notify TourManagementService to update participant count
            ResponseEntity<String> notificationResponse = tourManagementClient.notifyTourManagement(newBooking);

            if (notificationResponse.getStatusCode() != HttpStatus.OK) {
                // Roll back booking if TourManagementService fails
                bookingService.cancelBooking(newBooking.getBookingId());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Booking failed. Capacity validation at TourManagementService failed. Try again.");
            }

            return ResponseEntity.ok("Booking created successfully.");

        } catch (IllegalStateException e) {
            // deal with race conditions or capacity issues
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Booking failed because of concurrent requests or capacity issues. Try again.");
        } catch (IllegalArgumentException e) {
            // deal with invalid inputs or duplicate bookings
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
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
