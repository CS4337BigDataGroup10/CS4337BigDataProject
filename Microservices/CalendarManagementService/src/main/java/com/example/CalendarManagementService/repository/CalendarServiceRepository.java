package com.example.CalendarManagementService.repository;

import com.example.CalendarManagementService.entity.CalendarServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarServiceRepository extends JpaRepository<CalendarServiceEntity, Long> {

}