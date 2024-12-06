package com.example.BookingMakingService;

import com.example.BookingMakingService.contoller.BookingController;
import com.example.BookingMakingService.entity.Booking;
import com.example.BookingMakingService.entity.Tour;
import com.example.BookingMakingService.service.BookingService;
import com.example.BookingMakingService.service.TourManagementClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private TourManagementClient tourManagementClient;

    @InjectMocks
    private BookingController bookingController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test // Test to ensure all bookings are returned
    void testGetAllBookings() throws Exception {
        when(bookingService.getAllBookings()).thenReturn("[Booking1, Booking2]");

        mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(content().string("[Booking1, Booking2]"));

        verify(bookingService, times(1)).getAllBookings();
    }

    @Test
    public void testGetBookingsByEmailId() throws Exception {
        // Example: Mock the service method to return a specific string
        String emailId = "test@example.com";
        String bookings = "[Booking1]"; // Example string response from the service
        when(bookingService.getBookingsByEmailId(emailId)).thenReturn(bookings);

        // Perform the GET request and assert the response
        mockMvc.perform(get("/bookings/email/{emailId}", emailId))
                .andExpect(status().isOk())
                .andExpect(content().string(bookings)); // Assert that the returned content matches the mock string
    }

    @Test
    public void testNoBookingsFound() throws Exception {
        // Example: Mock the service method to return an empty response (no bookings)
        String emailId = "nonexistent@example.com";
        String bookings = "[]"; // This represents no bookings

        when(bookingService.getBookingsByEmailId(emailId)).thenReturn(bookings);

        // Perform the GET request and assert the response
        mockMvc.perform(get("/bookings/email/{emailId}", emailId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No bookings found for email ID: " + emailId));
    }

    @Test // Test creating a booking when the tour is available
    void testCreateBooking() throws Exception {
        Tour availableTour = new Tour();
        availableTour.setTourId(101);
        when(tourManagementClient.getNonFullTours()).thenReturn(List.of(availableTour));

        Booking booking = new Booking();
        booking.setBookingId(1);
        booking.setTourId(101);
        when(bookingService.createBooking(any(Booking.class))).thenReturn(booking);

        mockMvc.perform(post("/bookings/book/101")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Booking created successfully and notification sent."));

        verify(tourManagementClient, times(1)).getNonFullTours();
        verify(bookingService, times(1)).createBooking(any(Booking.class));
        verify(tourManagementClient, times(1)).notifyTourManagement(any(Booking.class));
    }

    @Test // Test creating a booking when the tour is not available
    void testCreateBooking_TourNotAvailable() throws Exception {
        when(tourManagementClient.getNonFullTours()).thenReturn(List.of());

        mockMvc.perform(post("/bookings/book/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Selected tour is not available."));

        verify(tourManagementClient, times(1)).getNonFullTours();
        verify(bookingService, never()).createBooking(any(Booking.class));
    }

    @Test // Test canceling a booking successfully
    void testCancelBooking() throws Exception {
        // Create a booking object
        Booking booking = new Booking();
        booking.setBookingId(1);
        booking.setEmailId("test@example.com");
        booking.setCancelled(false);  // booking is not yet canceled

        // Mock the repository method to return this booking
        when(bookingService.findBookingById(1)).thenReturn(booking);

        // Mock cancelBooking to return true (indicating successful cancellation)
        when(bookingService.cancelBooking(1)).thenReturn(true);

        // Perform the cancel booking action
        mockMvc.perform(put("/bookings/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(content().string("Booking with ID 1 has been cancelled."));

        // Verify interactions
        verify(bookingService, times(1)).cancelBooking(1);
        verify(tourManagementClient, times(1)).notifyCancellation(booking);
    }

    @Test // Test canceling a booking that is already canceled or non-existent
    void testCancelBooking_AlreadyCancelled() throws Exception {
        // Mock the repository to return a booking that's already cancelled
        Booking booking = new Booking();
        booking.setBookingId(1);
        booking.setEmailId("test@example.com");
        booking.setCancelled(true);  // booking is already canceled

        // Mock the behavior when booking is found
        when(bookingService.findBookingById(1)).thenReturn(booking);

        // Use lenient stubbing for cancelBooking to avoid unnecessary stubbing exception
        lenient().when(bookingService.cancelBooking(1)).thenReturn(false);  // cancellation should fail

        // Perform the cancel booking action
        mockMvc.perform(put("/bookings/1/cancel"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Booking with ID 1 is already cancelled or does not exist."));

        // Verify the cancelBooking method was never called if the booking is already canceled
        verify(bookingService, times(0)).cancelBooking(1);

        // Verify the tourManagementClient's notifyCancellation was not triggered
        verify(tourManagementClient, never()).notifyCancellation(any(Booking.class));
    }

    @Test // Test server error during booking creation
    void testCreateBooking_InternalServerError() throws Exception {
        Tour availableTour = new Tour();
        availableTour.setTourId(101);
        when(tourManagementClient.getNonFullTours()).thenReturn(List.of(availableTour));

        doThrow(new RuntimeException("Database error")).when(bookingService).createBooking(any(Booking.class));

        mockMvc.perform(post("/bookings/book/101")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error creating booking: Database error"));

        verify(tourManagementClient, times(1)).getNonFullTours();
        verify(bookingService, times(1)).createBooking(any(Booking.class));
    }
}
