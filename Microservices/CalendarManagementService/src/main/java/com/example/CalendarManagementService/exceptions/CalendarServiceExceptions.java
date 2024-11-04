package com.example.CalendarManagementService.exceptions;

public class CalendarServiceExceptions extends RuntimeException {
    public CalendarServiceExceptions(String message) {
        super(message);
    }

    public CalendarServiceExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}