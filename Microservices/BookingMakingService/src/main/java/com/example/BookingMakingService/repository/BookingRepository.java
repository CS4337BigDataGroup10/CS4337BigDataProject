package com.example.BookingMakingService.repository;

import com.example.BookingMakingService.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    // I am calling this method from BookingService. In booking service I am calling findAll() method.
    //This is a JpaRepository method that is used to fetch all the bookings from the database.

    // Custom query method to find all bookings by emailId
    List<Booking> findByEmailId(String emailId);
}
