package com.example.BookingMakingService;

import com.example.BookingMakingService.exceptions.BookingExceptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookingExceptionsTest {
    @Test
    void testBookingSearchException() {
        // Simulating the scenario where a booking is not found
        Exception exception = assertThrows(BookingExceptions.BookingSearchException.class, () -> {
            throw new BookingExceptions.BookingSearchException("Booking not found");
        });

        assertEquals("Booking not found", exception.getMessage());
    }

    @Test
    void testBookingSearchExceptionWithCause() {
        Throwable cause = new Exception("Underlying cause");
        String message = "Booking not found";
        BookingExceptions.BookingSearchException exception = new BookingExceptions.BookingSearchException(message, cause);

        assertEquals(message + " Throwable: No Booking found with that ID. Try creating the booking again or contact support", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
