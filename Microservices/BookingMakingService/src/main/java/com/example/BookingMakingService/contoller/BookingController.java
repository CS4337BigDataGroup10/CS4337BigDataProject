package com.example.BookingMakingService.contoller;

import com.example.BookingMakingService.BookingApp;
import com.example.BookingMakingService.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingController {

    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

  //  @GetMapping("/bookings"){
    //    return bookingService.getAllBookings();
    //}
}
