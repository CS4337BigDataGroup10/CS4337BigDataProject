package com.example.BookingMakingService.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;


    @Column(name = "emailId", nullable = false)
    private String emailId;


    @Column(name = "tourId", nullable = false)
    private int tour;

    public Booking() {}

    public Booking(int bookingId, String emailId, int tour){
        this.bookingId = bookingId;
        this.emailId = emailId;
        this.tour = tour;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getTour() {
        return tour;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setTour(int tour) {
        this.tour = tour;
    }
}
