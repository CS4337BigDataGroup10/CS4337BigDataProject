package com.example.TourManagementService.dto;

public class BookingNotificationDTO {
    private int bookingId;
    private int tourId;

    public BookingNotificationDTO(int bookingId, int tourId) {
        this.bookingId = bookingId;
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
}
