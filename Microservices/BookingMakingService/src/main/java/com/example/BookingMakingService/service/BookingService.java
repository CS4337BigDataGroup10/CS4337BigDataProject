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
    // New: Update a booking
    public Booking updateBooking(int id, Booking updatedBooking) {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));
        // Only update fields explicitly passed
        existingBooking.setBookingId(updatedBooking.getBookingId()); // Example field
        existingBooking.setEmailId(updatedBooking.getEmailId()); // Example field
        return bookingRepository.save(existingBooking);
    }

    // New: Delete a booking
    public void deleteBooking(int id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found with ID: " + id);
        }
        bookingRepository.deleteById(id);
    }
}
