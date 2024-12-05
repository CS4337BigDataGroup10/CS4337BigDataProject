package com.example.BookingMakingService.contoller;

import com.example.BookingMakingService.entity.Booking;
import com.example.BookingMakingService.entity.Tour;
import com.example.BookingMakingService.service.BookingService;
import com.example.BookingMakingService.service.TourManagementClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Booking Management", description = "APIs for managing bookings")
public class BookingController {

    private final BookingService bookingService;
    private final TourManagementClient tourManagementClient;

    public BookingController(BookingService bookingService, TourManagementClient tourManagementClient) {
        this.bookingService = bookingService;
        this.tourManagementClient = tourManagementClient;
    }

    @Operation(
            summary = "Get all bookings",
            description = "Retrieve a list of all current bookings",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved bookings",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/bookings")
    public ResponseEntity<String> getAllBookings() {
        String currentBookings = bookingService.getAllBookings();
        return ResponseEntity.ok(currentBookings);
    }

    @Operation(
            summary = "Get bookings by email ID",
            description = "Retrieve bookings for a specific user by their email ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved bookings"),
                    @ApiResponse(responseCode = "404", description = "No bookings found for the specified email ID")
            }
    )
    @GetMapping("/email/{emailId}")
    public ResponseEntity<String> getBookingsByEmailId(@PathVariable String emailId) {
        String userBookings = bookingService.getBookingsByEmailId(emailId).toString();
        if (userBookings.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userBookings);
    }

    @Operation(
            summary = "Create a new booking",
            description = "Create a new booking for a specified tour if the tour is available",
            requestBody = @RequestBody(description = "Booking details", content = @Content(schema = @Schema(implementation = Booking.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Booking created successfully"),
                    @ApiResponse(responseCode = "400", description = "Selected tour is not available")
            }
    )
    @PostMapping
    public ResponseEntity<String> createBooking(@RequestBody Booking booking) {
        try {
            List<Tour> availableTours = tourManagementClient.getNonFullTours();

            boolean tourAvailable = availableTours.stream()
                    .anyMatch(tour -> tour.getTourId() == booking.getTourId());

            if (!tourAvailable) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Selected tour is not available or is fully booked.");
            }

            // creating the booking locally
            Booking newBooking = bookingService.createBooking(booking);

            // notifying TourManagementService to update participant count
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

    @Operation(
            summary = "Cancel a booking",
            description = "Cancel a booking by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Booking cancelled successfully"),
                    @ApiResponse(responseCode = "400", description = "Booking is already cancelled or does not exist")
            }
    )

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
