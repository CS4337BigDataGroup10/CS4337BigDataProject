package com.example.BookingMakingService.entity;

import jakarta.persistence.*;
//merge conflict

@Entity
@Table(name = "Booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;

    @JoinColumn(name = "emailId", nullable = false)
    private String emailId; // user can just be a String emailid

    @JoinColumn(name = "tourId", nullable = false)
    private int tourId; // tour can just be an int tourID

    @Column(nullable = false)
    private boolean isCancelled = false; // Default is not cancelled (confirmed)


    // Getters and Setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { this.emailId = emailId; }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }
}