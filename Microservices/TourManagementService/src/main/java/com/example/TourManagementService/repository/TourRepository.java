package com.example.TourManagementService.repository;

import com.example.TourManagementService.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Integer> {

    @Query("SELECT t FROM Tour t WHERE t.participantCount < 20")
    List<Tour> findAvailableTours();

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END FROM Tour t WHERE t.tourDateTime LIKE :date%")
    boolean existsByTourDate(@Param("date") String date);
}