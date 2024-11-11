package com.example.TourManagementService.repository;

import com.example.TourManagementService.entity.TourBookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourBookingsRepository extends JpaRepository<TourBookings, Integer> {
}
