package com.example.TourManagementService.repository;

import com.example.TourManagementService.entity.TourBookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TourBookingsRepository extends JpaRepository<TourBookings, Integer> {

    Integer deletedByTourIdandBookingId(int tourId, int bookingId);

    @Modifying
    @Query("UPDATE TourBookings tb SET tb.tour.tourId = ?1 WHERE tb.bookingId = ?2")
    Integer updateTourBookingsByBookingIdAnd(int tourId, int bookingId);
}
