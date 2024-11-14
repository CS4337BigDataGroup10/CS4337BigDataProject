package com.example.BookingMakingService.service;

import com.example.BookingMakingService.entity.Booking;
import com.example.BookingMakingService.entity.TourBookings;
import com.example.BookingMakingService.repository.BookingRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    public Booking createBooking(Booking booking) {
        bookingRepository.save(booking);
        return booking;
    }
}
