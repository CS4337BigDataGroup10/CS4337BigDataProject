package com.example.TourManagementService.repository;

import com.example.TourManagementService.entity.TourBookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TourBookingsRepository extends JpaRepository<TourBookings, Integer> {

    @Modifying
    @Query("DELETE FROM TourBookings tb WHERE tb.tourId = ?1 AND tb.bookingId = ?2")
    void deleteByTourIdAndBookingId(int tourId, int bookingId);

    @Modifying
    @Query("UPDATE TourBookings tb SET tb.tourId = ?1 WHERE tb.bookingId = ?2")
    void updateTourBookingsByBookingIdAndBook(int tourId, int bookingId);

    boolean existsByTourIdAndBookingId(int tourId, int bookingId);
}
