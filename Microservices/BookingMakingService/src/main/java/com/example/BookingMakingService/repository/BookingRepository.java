package com.example.BookingMakingService.repository;

import com.example.BookingMakingService.entity.TourBookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<TourBookings, Integer> {

    @Query("SELECT t FROM TourBookings t WHERE t.bookingId != null")
    List<TourBookings> getAllBookings();
}
