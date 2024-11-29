package com.example.BookingMakingService;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.BookingMakingService.dto.BookingNotificationDTO;
import com.example.BookingMakingService.entity.Booking;
import com.example.BookingMakingService.repository.BookingRepository;
import com.example.BookingMakingService.service.BookingService;
import com.example.BookingMakingService.entity.Tour;
import com.example.BookingMakingService.service.TourManagementClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
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

    @Test
    void testCancelBooking(){
        Booking booking = new Booking();
        booking.setBookingId(1);
        booking.setEmailId("test@example.com");
        booking.setCancelled(false);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));

        boolean result = bookingService.cancelBooking(1);

        assertTrue(result);
        assertTrue(booking.isCancelled());
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void testCancelBookingAlreadyCancelled() {
        Booking booking = new Booking();
        booking.setBookingId(1);
        booking.setEmailId("test@example.com");
        booking.setCancelled(true);
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));

        boolean result = bookingService.cancelBooking(1);

        assertFalse(result);
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testCancelBookingNotFound() {
        when(bookingRepository.findById(1)).thenReturn(Optional.empty());

        boolean result = bookingService.cancelBooking(1);

        assertFalse(result); // No booking found
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testGetBookings(){
        Booking booking = new Booking();
        booking.setBookingId(1);
        booking.setEmailId("test@example.com");
        booking.setCancelled(true);
        when(bookingRepository.findByEmailId("test@example.com")).thenReturn(List.of(booking));

        String bookings = bookingService.getBookingsByEmailId("test@example.com");

        assertNotNull(bookings);
        verify(bookingRepository, times(1)).findByEmailId("test@example.com");

    }
}

