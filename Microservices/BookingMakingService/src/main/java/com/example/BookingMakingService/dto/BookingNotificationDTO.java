package com.example.BookingMakingService.dto;



public class BookingNotificationDTO {
    private int bookingId;
    private int tourId;
    private String emailId;

    public BookingNotificationDTO(int bookingId, String emailId, int tourId) {
        this.bookingId = bookingId;
        this.emailId = emailId;
        this.tourId = tourId;
    }

    public BookingNotificationDTO() {
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getTourId() {
        return tourId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }
}
