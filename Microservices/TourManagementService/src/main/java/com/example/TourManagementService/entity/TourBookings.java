package com.example.TourManagementService.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "TourBookings")
public class TourBookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "bookingId", nullable = false)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "tourId", nullable = false)
    private Tour tour;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }

    public Tour getTour() { return tour; }
    public void setTour(Tour tour) { this.tour = tour; }
}