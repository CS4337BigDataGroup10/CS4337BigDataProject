package com.example.BookingMakingService.exceptions;

public class BookingExceptions {

    public static class BookingSearchException extends RuntimeException {
        public BookingSearchException(String message) {
            super(message);
        }

        public BookingSearchException(String message, Throwable cause) {
            super(message + " Throwable: No Booking found with that ID. Try creating the booking again or contact support", cause);
        }
    }
    }
