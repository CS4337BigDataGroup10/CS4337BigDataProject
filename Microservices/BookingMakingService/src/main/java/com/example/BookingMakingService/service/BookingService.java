package com.example.BookingMakingService.service;

import com.example.BookingMakingService.entity.Booking;
import com.example.BookingMakingService.repository.BookingRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingService {
    @Autowired
    private final BookingRepository bookingRepository;
    private Logger log;

    //Constructor
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
    //this is querying my bookingrepositroy db to get all the bookings
    public String getAllBookings() {
        return bookingRepository.findAll().toString();
    }

    // New method to get bookings by emailId
    public String getBookingsByEmailId(String emailId) {
        List<Booking> bookings = bookingRepository.findByEmailId(emailId);

        if (bookings == null || bookings.isEmpty()) {
            return "[]";  // Empty list response in string format, adjust if necessary
        }
        return bookings.toString();  // Converts bookings list to string representation
    }
    //this is saving the booking to the db
    //.save() is a JpaRepository method that is used to save the booking to the database.
    public Booking findBookingById(int bookingId){return bookingRepository.findById(bookingId).get();}

    public Booking createBooking(Booking booking) {
        if (bookingRepository.existsByTourIdAndEmailId(booking.getTourId(), booking.getEmailId())) {
            throw new IllegalArgumentException("Booking already exists for this user and tour.");
        }
        bookingRepository.save(booking);
        return booking;
    }

    public boolean cancelBooking(int bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);

        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();

            if (!booking.isCancelled()) {  // Proceed only if the booking is not already cancelled
                booking.setCancelled(true);
                bookingRepository.save(booking);
                return true;
            }
        }
        return false;
    }
}
