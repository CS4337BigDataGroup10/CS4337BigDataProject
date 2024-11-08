package com.example.UserManagementService.entity;

import java.time.LocalDateTime;

public class CustomerEntity extends UserEntity {

    private String bookingDetails;
    private int bookingSize;

    public CustomerEntity() {
        super();
    }

    public CustomerEntity(Long id, String name, String email, Role role,
                          String bookingDetails, int bookingSize) {
        super(id, name, email, role);
        this.bookingDetails = bookingDetails;
        this.bookingSize = bookingSize;
    }

    public String getBookingDetails() {
        return bookingDetails;
    }

    public void setBookingDetails(String bookingDetails) {
        this.bookingDetails = bookingDetails;
    }

    public int getBookingSize() {
        return bookingSize;
    }

    public void setBookingSize(int bookingSize) {
        this.bookingSize = bookingSize;
    }
}

