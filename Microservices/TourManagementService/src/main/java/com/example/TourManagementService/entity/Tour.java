package com.example.TourManagementService.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Tour")
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tourId;

    @Column(nullable = true)
    private String emailId;

    @Column(nullable = false)
    private String tourDateTime;

    @Column(nullable = false)
    private int participantCount;

    @Column(nullable = false)
    private int maxCapacity;

    // Getters and Setters
    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getTourDateTime() {
        return tourDateTime;
    }

    public void setTourDateTime(String tourDateTime) {
        this.tourDateTime = tourDateTime;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
}
