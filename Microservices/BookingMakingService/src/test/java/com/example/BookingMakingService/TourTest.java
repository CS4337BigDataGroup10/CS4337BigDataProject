package com.example.BookingMakingService;

import com.example.BookingMakingService.entity.Tour;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TourTest {

    @Test
    void testTourSettersAndGetters() {
        Tour tour = new Tour();

        tour.setTourId(101);
        tour.setEmailId("example@example.com");
        tour.setTourDateTime("2024-12-10T10:00:00");
        tour.setParticipantCount(20);
        tour.setTourGuideId("guide123");

        assertEquals(101, tour.getTourId());
        assertEquals("example@example.com", tour.getEmailId());
        assertEquals("2024-12-10T10:00:00", tour.getTourDateTime());
        assertEquals(20, tour.getParticipantCount());
        assertEquals("guide123", tour.getTourGuideId());
    }

    @Test
    void testDefaultConstructor() {
        Tour tour = new Tour();

        assertNotNull(tour);
        assertEquals(0, tour.getTourId());
        assertNull(tour.getEmailId());
        assertNull(tour.getTourDateTime());
        assertEquals(0, tour.getParticipantCount());
        assertNull(tour.getTourGuideId());
    }

    @Test
    void testUpdateParticipantCount() {
        Tour tour = new Tour();

        tour.setParticipantCount(18);
        assertEquals(18, tour.getParticipantCount());
    }

}
