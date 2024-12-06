package com.example.BookingMakingService.dto;



public class BookingNotificationDTO {
    private int bookingId;
    private int tourId;
    private boolean iscancelled;
    private String email;

    public BookingNotificationDTO(int bookingId, int tourId, boolean iscancelled, String email) {
        this.bookingId = bookingId;
        this.tourId = tourId;
        this.iscancelled = iscancelled;
        this.email = email;
    }

    public BookingNotificationDTO() {
    }

    public boolean getIsIscancelled(){return iscancelled;}
    public void setiscancelled(boolean iscancelled){
        this.iscancelled = iscancelled;
    }
    public int getBookingId() {
        return bookingId;
    }

    public int getTourId() {
        return tourId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public void setEmail(String emailId) {
        this.email=emailId;
    }
}
