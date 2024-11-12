package com.example.BookingMakingService.contoller;

import com.example.BookingMakingService.entity.Booking;
import com.example.BookingMakingService.service.BookingService;
import com.example.BookingMakingService.service.TourManagementClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    public ResponseEntity<String> createBooking(@RequestBody Booking booking) {
        Booking newBooking = bookingService.createBooking(booking);
        tourManagementClient.notifyTourManagement(newBooking);
        return ResponseEntity.ok("Booking created successfully and notification sent.");
    }

    
}
