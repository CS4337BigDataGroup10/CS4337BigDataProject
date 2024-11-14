package com.example.BookingMakingService.repository;

import com.example.BookingMakingService.entity.Booking;
import com.example.BookingMakingService.entity.TourBookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("SELECT * FROM Booking")
    List<TourBookings> getAllBookings();

    Optional<Booking> findById(int bookingId);
    Optional<Booking> findByTourId(int tourId);
}
