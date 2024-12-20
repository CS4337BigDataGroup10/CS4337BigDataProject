package com.example.BookingMakingService.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "TourBookings")
public class TourBookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "bookingId", nullable = false)
    private int bookingId;

    @JoinColumn(name = "tourId", nullable = false)
    private int tourId;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getTourId() { return this.tourId; }
    public int setTourId(int new_tourId) {
        this.tourId = new_tourId;
        return new_tourId;
    }
}