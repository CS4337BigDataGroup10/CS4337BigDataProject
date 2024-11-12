package com.example.BookingMakingService.entity;

import com.example.TourManagementService.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "Booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;

    @ManyToOne
    @JoinColumn(name = "emailId", nullable = false)
    private User user;

    @Column(nullable = false)
    private int size; // Number of participants in this booking

    // Getters and Setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
}