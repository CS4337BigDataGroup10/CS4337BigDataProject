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
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final TourManagementClient tourManagementClient;
    private static int counter = 1;
    public BookingController(BookingService bookingService, TourManagementClient tourManagementClient) {
        this.bookingService = bookingService;
        this.tourManagementClient = tourManagementClient;
    }

    @GetMapping("/currentbookings")
    public ResponseEntity<String> getAllBookings() {
        String currentBookings = bookingService.getAllBookings();
        return ResponseEntity.ok(currentBookings);
    }

    // New endpoint to get bookings by emailId
    @GetMapping("/email/{emailId}")
    public ResponseEntity<String> getBookingsByEmailId(@PathVariable String emailId) {
        String userBookings = bookingService.getBookingsByEmailId(emailId);
        if (userBookings.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userBookings);
    }

    @PostMapping("/book/{tourid}")
    public ResponseEntity<String> createBooking(@PathVariable Long tourid) {
        // Fetch all available tours
        List<Tour> availableTours = tourManagementClient.getNonFullTours();
        System.out.println("Got all tours");
        //Check if the requested tour is available
        boolean tourAvailable = availableTours.stream()
                .anyMatch(tour -> tour.getTourId() == tourid);
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
        System.out.println("Creating Booking");
        Booking newBooking = bookingService.createBooking(booking);
        System.out.println("Notifying tour management");
        tourManagementClient.notifyTourManagement(newBooking); //this notifys tour management service that the booking has been created after going through the checks.
        return ResponseEntity.ok("Booking created successfully and notification sent.");
    }

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
