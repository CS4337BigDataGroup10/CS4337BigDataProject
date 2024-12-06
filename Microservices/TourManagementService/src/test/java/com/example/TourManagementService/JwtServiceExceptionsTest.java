package com.example.TourManagementService;

import com.example.TourManagementService.exceptions.JwtServiceExceptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class JwtServiceExceptionsTest {

    @Test
    void testTokenExpiredExceptionWithMessage() {
        String message = "Token has expired.";
        JwtServiceExceptions.TokenExpiredException exception =
                new JwtServiceExceptions.TokenExpiredException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testTokenExpiredExceptionWithMessageAndCause() {
        String message = "Token has expired.";
        Throwable cause = new RuntimeException("Cause of the exception");
        JwtServiceExceptions.TokenExpiredException exception =
                new JwtServiceExceptions.TokenExpiredException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testInvalidTokenExceptionWithMessage() {
        String message = "Token is invalid.";
        JwtServiceExceptions.InvalidTokenException exception =
                new JwtServiceExceptions.InvalidTokenException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testInvalidTokenExceptionWithMessageAndCause() {
        String message = "Token is invalid.";
        Throwable cause = new RuntimeException("Cause of the exception");
        JwtServiceExceptions.InvalidTokenException exception =
                new JwtServiceExceptions.InvalidTokenException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
