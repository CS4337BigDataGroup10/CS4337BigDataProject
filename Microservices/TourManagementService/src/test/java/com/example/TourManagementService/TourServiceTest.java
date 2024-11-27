//package com.example.TourManagementService;
//
//import com.example.TourManagementService.dto.BookingNotificationDTO;
//import com.example.TourManagementService.entity.Tour;
//import com.example.TourManagementService.entity.TourBookings;
//import com.example.TourManagementService.exceptions.CapacityExceededException;
//import com.example.TourManagementService.repository.TourRepository;
//import com.example.TourManagementService.service.TourScheduler;
//import com.example.TourManagementService.service.TourService;
//import com.example.TourManagementService.repository.TourBookingsRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@SpringBootTest
//@Transactional
//public class TourServiceTest {
//
//    @Autowired
//    private TourService tourService;
//
//    @Autowired
//    private TourRepository tourRepository;
//
//    @Autowired
//    private TourBookingsRepository TourBookingsRepository;
//
//    @Test
//    public void testGetTourById_TourExists() {
//        // Arrange
//        Tour tour = new Tour();
//        tour.setEmailId("test@example.com");
//        tour.setTourDateTime("2024-01-01T09:00:00");
//        tour.setParticipantCount(0);
//        tour = tourRepository.save(tour);
//
//        // Act
//        Tour fetchedTour = tourService.getTourById(tour.getTourId());
//
//        // Assert
//        Assertions.assertNotNull(fetchedTour);
//        Assertions.assertEquals("test@example.com", fetchedTour.getEmailId());
//    }
//
//    @Test
//    void testUpdateParticipantCount_CapacityExceeded() {
//        Tour tour = new Tour();
//        tour.setEmailId("guide@example.com");
//        tour.setTourDateTime(LocalDateTime.now().toString());
//        tour.setParticipantCount(19); // Initial participant count
//        tour.setMaxCapacity(20);
//
//        // Save the tour to the repository
//        tour = tourRepository.save(tour);
//
//        // Expect CapacityExceededException when exceeding capacity
//        Tour finalTour = tour;
//        assertThrows(CapacityExceededException.class, () -> {
//            tourService.updateParticipantCount(finalTour.getTourId(), 2); // Adding 2 exceeds capacity
//        });
//    }
//
//    @Test
//    void testAddBooking_Success() {
//        Tour tour = new Tour();
//        tour.setEmailId("guide@example.com");
//        tour.setTourDateTime(LocalDateTime.now().toString());
//        tour.setParticipantCount(18);
//        tour.setMaxCapacity(20);
//        tour = tourRepository.save(tour);
//
//        tourService.updateParticipantCount(tour.getTourId(), 2);
//
//        Tour updatedTour = tourRepository.findById(tour.getTourId()).orElseThrow();
//        Assertions.assertEquals(20, updatedTour.getParticipantCount());
//    }
//
//    @Test
//    void testRemoveBooking() {
//        Tour tour = new Tour();
//        tour.setEmailId("guide@example.com");
//        tour.setTourDateTime(LocalDateTime.now().toString());
//        tour.setParticipantCount(5);
//        tour.setMaxCapacity(20);
//        tour = tourRepository.save(tour);
//
//        TourBookings booking = new TourBookings();
//        booking.setTourId(tour.getTourId());
//        booking.setBookingId(1);
//        TourBookingsRepository.save(booking);
//
//        //Remove the booking
//        tourService.removeBooking(tour.getTourId(), 1);
//
//        //Verify the participant count and that the booking is removed
//        Tour updatedTour = tourRepository.findById(tour.getTourId()).orElseThrow();
//        Assertions.assertEquals(4, updatedTour.getParticipantCount(), "Participant count should decrease by 1");
//        Assertions.assertFalse(TourBookingsRepository.existsById(1), "Booking should be removed from the repository");
//    }
//
//    @Test
//    void testRemoveBooking_NotFound() {
//        Tour tour = new Tour();
//        tour.setEmailId("guide@example.com");
//        tour.setTourDateTime(LocalDateTime.now().toString());
//        tour.setParticipantCount(5);
//        tour.setMaxCapacity(20);
//        tour = tourRepository.save(tour);
//
//        Tour finalTour = tour;
//        assertThrows(IllegalArgumentException.class, () -> {
//            tourService.removeBooking(finalTour.getTourId(), 6969);
//        });
//    }
//    @Test
//    void testSaveTourBooking() {
//        TourBookings booking = new TourBookings();
//        booking.setTourId(1);
//        booking.setBookingId(1);
//        TourBookings savedBooking = TourBookingsRepository.save(booking);
//        Assertions.assertNotNull(savedBooking.getId(), "Saved booking ID should not be null.");
//    }
//
//    @Test
//    void testHandleNewBooking() {
//        Tour tour = new Tour();
//        tour.setEmailId("guide@example.com");
//        tour.setTourDateTime(LocalDateTime.now().toString());
//        tour.setParticipantCount(0);
//        tour.setMaxCapacity(20);
//        tour = tourRepository.save(tour);
//
//        BookingNotificationDTO bookingNotificationDto = new BookingNotificationDTO();
//        bookingNotificationDto.setTourId(tour.getTourId());
//        bookingNotificationDto.setBookingId(1);
//
//        System.out.println("BookingNotificationDTO: " + bookingNotificationDto);
//
//        tourService.handleNewBooking(bookingNotificationDto);
//
//        System.out.println("All bookings after save: " + TourBookingsRepository.findAll());
//
//        Tour finalTour = tour;
//        Assertions.assertTrue(
//                TourBookingsRepository.findAll().stream()
//                        .anyMatch(booking -> booking.getTourId() == finalTour.getTourId() && booking.getBookingId() == 1),
//                "Booking should have been saved in the repository."
//        );
//    }
//
//    @Test
//    void testPreventDuplicateBookings() {
//        Tour tour = new Tour();
//        tour.setEmailId("guide@example.com");
//        tour.setTourDateTime(LocalDateTime.now().toString());
//        tour.setParticipantCount(0);
//        tour.setMaxCapacity(20);
//        tour = tourRepository.save(tour);
//
//        BookingNotificationDTO bookingNotificationDto1 = new BookingNotificationDTO();
//        bookingNotificationDto1.setTourId(tour.getTourId());
//        bookingNotificationDto1.setBookingId(1);
//
//        tourService.handleNewBooking(bookingNotificationDto1);
//
//        Tour finalTour1 = tour;
//        Assertions.assertTrue(
//                TourBookingsRepository.findAll().stream()
//                        .anyMatch(booking -> booking.getTourId() == finalTour1.getTourId() && booking.getBookingId() == 1),
//                "Booking should have been saved in the repository."
//        );
//
//        // trying to insert a duplicate booking
//        BookingNotificationDTO bookingNotificationDto2 = new BookingNotificationDTO();
//        bookingNotificationDto2.setTourId(tour.getTourId());
//        bookingNotificationDto2.setBookingId(1);
//
//        // check that an IllegalArgumentException is thrown due to the duplicate booking
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            tourService.handleNewBooking(bookingNotificationDto2); // Should throw exception due to duplicate
//        });
//
//        Assertions.assertTrue(exception.getMessage().contains("Booking already exists for the given tour"));
//
//        // Verify that no new booking was added
//        Tour finalTour = tour;
//        long bookingCount = TourBookingsRepository.findAll().stream()
//                .filter(booking -> booking.getTourId() == finalTour.getTourId() && booking.getBookingId() == 1)
//                .count();
//        Assertions.assertEquals(1, bookingCount, "Duplicate booking should not be added.");
//    }
//
//}
