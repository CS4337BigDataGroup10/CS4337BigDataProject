package com.example.BigDataProject;

public class Booking {

    // Private variables
    private int bookingID;
    private int tourID;
    private int bookingSize;
    private String customerEmail;

    // Constructor
    public Booking(int bookingID, int tourID, int bookingSize, String customerEmail) {
        this.bookingID = bookingID;
        this.tourID = tourID;
        this.bookingSize = bookingSize;
        this.customerEmail = customerEmail;
    }

    public int getBookingID() {
        return bookingID;
    }
    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getTourID() {
        return tourID;
    }

    public void setTourID(int tourID) {
        this.tourID = tourID;
    }

    public int getBookingSize() {
        return bookingSize;
    }

    public void setBookingSize(int bookingSize) {
        this.bookingSize = bookingSize;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void displayBookingDetails() {
        System.out.println("Booking ID: " + bookingID);
        System.out.println("Tour ID: " + tourID);
        System.out.println("Booking Size: " + bookingSize);
        System.out.println("Customer Email: " + customerEmail);
    }

}

