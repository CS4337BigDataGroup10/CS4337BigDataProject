package com.example.BookingMakingService;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.BookingMakingService.entity.Booking;
import com.example.BookingMakingService.repository.BookingRepository;
import com.example.BookingMakingService.service.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class) // Enables Mockito
class BookingMakingServiceApplicationTests {
    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void testGetAllBookings() {
        when(bookingRepository.findAll()).thenReturn(Collections.emptyList());

        String result = bookingService.getAllBookings();

        assertEquals("[]", result);
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void testCreateBooking() {
        Booking booking = new Booking();
        booking.setBookingId(1);
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking result = bookingService.createBooking(booking);

        assertNotNull(result);
        assertEquals(1, result.getBookingId());

        verify(bookingRepository, times(1)).save(booking);
    }
}
