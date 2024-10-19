package com.example.BigDataProject;

import java.util.ArrayList;

public class Customer {

    // Private variables
    private String emailID;
    private String name;
    private String number;
    private ArrayList<Booking> bookings;

    public Customer(String emailID, String name, String number) {
        this.emailID = emailID;
        this.name = name;
        this.number = number;
        this.bookings = new ArrayList<>();  // Initialize the bookings list
    }

    // Method to view available tours
    public void viewTours() {

    }

    // Method to make a booking
    public void makeBooking(Tour tour) {
        // Add booking to database and reduce tour capacity by booking size
    }

    // Method to delete a booking
    public void deleteBooking(Booking booking) {
        // Check Database for booking then delete it from the database

    }

    // Method to view all bookings
    public void viewBookings() {
        if (bookings.isEmpty()) {
        } else {
            // Check database for bookings
        }
    }

    // Method to edit customer info
    public void editInfo(String newEmail, String newName, String newNumber) {
        // Update database with info
    }

    // Getter methods for emailID, name, and number
    public String getEmailID() {
        return emailID;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    // Setter methods for emailID, name, and number
    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
