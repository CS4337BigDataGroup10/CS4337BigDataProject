package com.example.BigDataProject;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Tour {

    private int tourID;
    private int capacity;
    private Timestamp timestamp;
    private ArrayList<Booking> bookings;
    private TourGuide tourGuide;

    // Constructor
    public Tour(int tourID, int capacity, Timestamp timestamp, TourGuide tourGuide) {
        this.tourID = tourID;
        this.capacity = 12; // Standard Tour Capacity
        this.timestamp = timestamp;
        this.bookings = new ArrayList<>();  // Initialize bookings list
        this.tourGuide = tourGuide;         // Assign the tour guide
    }

    public int getTourID() {
        return tourID;
    }

    public void setTourID(int tourID) {
        this.tourID = tourID;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(ArrayList<Booking> bookings) {
        this.bookings = bookings;
    }

    public TourGuide getTourGuide() {
        return tourGuide;
    }

    public void setTourGuide(TourGuide tourGuide) {
        this.tourGuide = tourGuide;
    }



    // Method to check if the tour is full
    /*public  isFull() {
        return getTotalBookingSize() >= capacity;
    }*/

    // Method to display tour details
    public void displayTourDetails() {
        System.out.println("Tour ID: " + tourID);
        System.out.println("Capacity: " + capacity);
        System.out.println("Timestamp: " + timestamp);
        System.out.println("Tour Guide: " + tourGuide.getName());
        System.out.println("Current Bookings: " + bookings.size() + "/" + capacity);
    }

}

