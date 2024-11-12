package com.example.BookingMakingService.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public class BookingRepository extends JpaRepository<TourBookings, Integer> {
    @Modifying
    @Query("DELETE FROM TourBookings t WHERE t.bookingId = ?2")
    void deleteBooking(int tourId, int bookingId) {

    }


    void addBooking(int tourId, int bookingId);


}
