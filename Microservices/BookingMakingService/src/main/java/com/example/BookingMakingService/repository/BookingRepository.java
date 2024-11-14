package com.example.BookingMakingService.repository;

import com.example.BookingMakingService.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    // I am calling this method from BookingService. In booking service I am calling findAll() method.
    //This is a JpaRepository method that is used to fetch all the bookings from the database.
}
