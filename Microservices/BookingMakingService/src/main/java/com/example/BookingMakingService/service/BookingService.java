package com.example.BookingMakingService.service;

import com.example.BookingMakingService.entity.Booking;
import com.example.BookingMakingService.repository.BookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    //Constructor
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
    //this is querying my bookingrepositroy db to get all the bookings
    public String getAllBookings() {
        return bookingRepository.findAll().toString();
    }
    //this is saving the booking to the db
    //.save() is a JpaRepository method that is used to save the booking to the database.
    @Transactional
    public Booking createBooking(Booking booking) {
        bookingRepository.save(booking);
        return booking;
    }
}
