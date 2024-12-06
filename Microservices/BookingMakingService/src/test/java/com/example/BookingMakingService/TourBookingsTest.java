package com.example.BookingMakingService;

import com.example.BookingMakingService.entity.TourBookings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TourBookingsTest {

    @Test
    void testTourBookingsSettersAndGetters() {
        TourBookings tourBookings = new TourBookings();

        tourBookings.setId(1);
        tourBookings.setBookingId(101);
        tourBookings.setTourId(202);

        assertEquals(1, tourBookings.getId());
        assertEquals(101, tourBookings.getBookingId());
        assertEquals(202, tourBookings.getTourId());
    }

    @Test
    void testDefaultConstructor() {
        TourBookings tourBookings = new TourBookings();

        assertNotNull(tourBookings);
        assertEquals(0, tourBookings.getId());
        assertEquals(0, tourBookings.getBookingId());
        assertEquals(0, tourBookings.getTourId());
    }

}

