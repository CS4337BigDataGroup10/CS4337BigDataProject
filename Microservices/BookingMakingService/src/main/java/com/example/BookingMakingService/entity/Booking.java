package com.example.BookingMakingService.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;

    @ManyToOne
    @JoinColumn(name = "emailId", nullable = false)
    private String emailId; // user can just be a String emailid

    @ManyToOne
    @JoinColumn(name = "tourId", nullable = false)
    private int tourId; // tour can just be an int tourID

    //@Column(nullable = false)
    //private int size; This is now deprecated as booking is only 1 person, tour takes care of the capacity

    // Getters and Setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { this.emailId = emailId; }

    /*public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }*/ // deprecated

    public int getTourId() {
        return tourId;
    }
}