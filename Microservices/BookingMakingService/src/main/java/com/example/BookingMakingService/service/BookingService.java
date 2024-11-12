package com.example.BookingMakingService.service;

import com.example.BookingMakingService.entity.Booking;
import com.example.BookingMakingService.repository.BookingRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public String getAllBookings() {
        return bookingRepository.getAllBookings().toString();
    }

    public Booking createBooking(Booking booking) {
        return new Booking();
    }
}
