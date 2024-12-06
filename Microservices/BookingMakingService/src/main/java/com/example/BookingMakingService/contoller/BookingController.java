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
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final TourManagementClient tourManagementClient;
    private static int counter = 1;
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
        String userBookings = bookingService.getBookingsByEmailId(emailId);
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
    @PostMapping("/book/{tourId}")
    public ResponseEntity<String> createBooking(@PathVariable("tourId") int tourId) {
        // Fetch all available tours
        System.out.println(tourId);
        List<Tour> availableTours = tourManagementClient.getNonFullTours();
        System.out.println("Got all tours");
        //Check if the requested tour is available
        boolean tourAvailable = availableTours.stream()
                .anyMatch(tour -> tour.getTourId() == tourId);
        if(tourAvailable) {
            System.out.println("Tour is available to be booked ");
        }
                //this is checking if the tour we are trying to book into is available and there. If it is not available, it will return a bad request
        if (!tourAvailable) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Selected tour is not available.");
        }
        System.out.println("Proceeding with booking creation");
        // Proceed with booking creation
        //Adds to db of booking repository
        Booking booking = new Booking();
        booking.setEmailId("test");
        booking.setCancelled(false);
        booking.setBookingId(counter);
        counter++;
        try {
            System.out.println("Creating Booking");
            Booking newBooking = bookingService.createBooking(booking);
            System.out.println("Notifying tour management");
            tourManagementClient.notifyTourManagement(newBooking); //this notifys tour management service that the booking has been created after going through the checks.
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating booking" +e.getMessage());
        }

        return ResponseEntity.ok("Booking created successfully and notification sent.");
    }

    @Operation(
            summary = "Cancel a booking",
            description = "Cancel a booking by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Booking cancelled successfully"),
                    @ApiResponse(responseCode = "400", description = "Booking is already cancelled or does not exist")
            }
    )

    // New endpoint to cancel a booking
    @PutMapping("/bookings/{bookingId}/cancel")
    public ResponseEntity<String> cancelBooking(@PathVariable int bookingId) {
        boolean isCancelled = bookingService.cancelBooking(bookingId);

        if (isCancelled) {
            tourManagementClient.notifyCancellation(bookingService.findBookingById(bookingId));
            return ResponseEntity.ok("Booking with ID " + bookingId + " has been cancelled.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Booking with ID " + bookingId + " is already cancelled or does not exist.");
        }
    }



}
