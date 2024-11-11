package com.example.UserManagementService.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TourGuideEntity extends UserEntity {

    private List<Long> assignedTours; // IDs of assigned tours
    private List<LocalDate> leaveDates; // List of leave dates

    public TourGuideEntity() {
        super();
    }

    public TourGuideEntity(Long id, String name, String email, String passwordHash, Role role,
                           LocalDateTime createdAt, LocalDateTime updatedAt,
                           List<Long> assignedTours, List<LocalDate> leaveDates) {
        super(id, name, email, role);
        this.assignedTours = assignedTours;
        this.leaveDates = leaveDates;
    }

    public List<Long> getAssignedTours() {
        return assignedTours;
    }

    public void setAssignedTours(List<Long> assignedTours) {
        this.assignedTours = assignedTours;
    }

    public List<LocalDate> getLeaveDates() {
        return leaveDates;
    }

    public void setLeaveDates(List<LocalDate> leaveDates) {
        this.leaveDates = leaveDates;
    }
}

