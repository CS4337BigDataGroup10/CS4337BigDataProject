/*
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
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

    @Test  // Test to ensure all bookings are returned
    void testGetAllBookings() throws Exception {
        List<Booking> bookings = Arrays.asList(new Booking(), new Booking());
        when(bookingService.getAllBookings()).thenReturn("[Booking1, Booking2]");

        mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(content().string("[Booking1, Booking2]"));
    }

    @Test // Tests to see if the correct booking is returned when searching by email
    void testGetBookingsByEmailId() throws Exception {
        Booking booking = new Booking();
        booking.setBookingId(1);
        booking.setEmailId("test@example.com");
        when(bookingService.getBookingsByEmailId("test@example.com")).thenReturn("[Booking1]");

        mockMvc.perform(get("/email/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("[Booking1]"));
    }

    @Test //Test for non-existent email
    void testGetBookingsByEmailId_NotFound() throws Exception {
        when(bookingService.getBookingsByEmailId("nonexistent@example.com")).thenReturn("[]");

        mockMvc.perform(get("/email/nonexistent@example.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateBooking() throws Exception {
        Booking booking = new Booking();
        booking.setBookingId(1);
        booking.setTourId(101);
        Tour tour = new Tour();
        tour.setTourId(101);
        when(tourManagementClient.getNonFullTours()).thenReturn(Arrays.asList(tour));
        when(bookingService.createBooking(booking)).thenReturn(booking);

        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content("{\"tourId\":101, \"emailId\":\"test@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Booking created successfully and notification sent."));

        verify(tourManagementClient, times(1)).notifyTourManagement(booking);
    }

    @Test
    void testCreateBooking_TourNotAvailable() throws Exception {
        // Arrange
        Booking booking = new Booking();
        booking.setBookingId(1);
        booking.setTourId(999);
        when(tourManagementClient.getNonFullTours()).thenReturn(Arrays.asList(new Tour()));

        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content("{\"tourId\":999, \"emailId\":\"test@example.com\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Selected tour is not available."));
    }

    @Test
    void testCancelBooking() throws Exception {
        Booking booking = new Booking();
        booking.setBookingId(1);
        booking.setEmailId("test@example.com");
        booking.setCancelled(false);
        when(bookingService.cancelBooking(1)).thenReturn(true);

        mockMvc.perform(put("/bookings/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(content().string("Booking with ID 1 has been cancelled."));
    }

    @Test
    void testCancelBooking_AlreadyCancelled() throws Exception {
        Booking booking = new Booking();
        booking.setBookingId(1);
        booking.setCancelled(true);
        when(bookingService.cancelBooking(1)).thenReturn(false);

        mockMvc.perform(put("/bookings/1/cancel"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Booking with ID 1 is already cancelled or does not exist."));
    }
}
*/
