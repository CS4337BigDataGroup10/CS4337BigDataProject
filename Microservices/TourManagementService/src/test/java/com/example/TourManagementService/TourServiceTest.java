package com.example.TourManagementService;

import com.example.TourManagementService.dto.BookingNotificationDTO;
import com.example.TourManagementService.entity.Tour;
import com.example.TourManagementService.entity.TourBookings;
import com.example.TourManagementService.exceptions.CapacityExceededException;
import com.example.TourManagementService.repository.TourRepository;
import com.example.TourManagementService.repository.TourBookingsRepository;
import com.example.TourManagementService.service.TourService;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TourServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TourRepository tourRepository;

    @Mock
    private TourBookingsRepository tourBookingsRepository;

    @InjectMocks
    private TourService tourService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTourById_TourExists() {
        Tour tour = new Tour();
        tour.setTourId(1);
        tour.setEmailId("test@example.com");
        when(tourRepository.findById(1)).thenReturn(Optional.of(tour));

        Tour fetchedTour = tourService.getTourById(1);

        assertNotNull(fetchedTour);
        assertEquals("test@example.com", fetchedTour.getEmailId());
        verify(tourRepository, times(1)).findById(1);
    }

    @Test
    void testUpdateParticipantCount_CapacityExceeded() {
        Tour tour = new Tour();
        tour.setTourId(1);
        tour.setParticipantCount(19);
        tour.setMaxCapacity(20);
        when(tourRepository.findById(1)).thenReturn(Optional.of(tour));

        CapacityExceededException exception = assertThrows(CapacityExceededException.class, () -> {
            tourService.updateParticipantCount(1, 2);
        });

        assertEquals("Cannot add booking, tour capacity exceeded.", exception.getMessage());
        verify(tourRepository, times(1)).findById(1);
    }

    @Test
    void testAddBooking_Success() {
        Tour tour = new Tour();
        tour.setTourId(1);
        tour.setParticipantCount(18);
        tour.setMaxCapacity(20);
        tour.setVersion(1); // Initial version

        when(tourRepository.findById(1)).thenReturn(Optional.of(tour));
        when(tourRepository.save(any(Tour.class))).thenAnswer(invocation -> {
            Tour savedTour = invocation.getArgument(0);
            savedTour.setVersion(2); // Simulate version increment after save
            return savedTour;
        });

        tourService.updateParticipantCount(1, 2);

        verify(tourRepository, times(1)).save(tour);
        assertEquals(20, tour.getParticipantCount());
        assertEquals(2, tour.getVersion()); // Ensure version increment
    }
    @Test
    void testRemoveBooking_Success() {
        Tour tour = new Tour();
        tour.setTourId(1);
        tour.setParticipantCount(5);
        when(tourRepository.findById(1)).thenReturn(Optional.of(tour));
        when(tourBookingsRepository.existsById(1)).thenReturn(true);

        tourService.removeBooking(1, 1);

        assertEquals(4, tour.getParticipantCount());
        verify(tourBookingsRepository, times(1)).deleteByTourIdAndBookingId(1, 1);
        verify(tourRepository, times(1)).save(tour);
    }

    @Test
    void testRemoveBooking_NotFound() {
        Tour tour = new Tour();
        tour.setTourId(1);
        when(tourRepository.findById(1)).thenReturn(Optional.of(tour));
        when(tourBookingsRepository.existsById(6969)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            tourService.removeBooking(1, 6969);
        });

        assertEquals("Booking not found with ID: 6969", exception.getMessage());
        verify(tourBookingsRepository, never()).deleteByTourIdAndBookingId(anyInt(), anyInt());
    }

    @Test
    void testHandleNewBooking_Success() {
        BookingNotificationDTO bookingNotificationDto = new BookingNotificationDTO();
        bookingNotificationDto.setTourId(1);
        bookingNotificationDto.setBookingId(1);

        when(tourBookingsRepository.existsByTourIdAndBookingId(1, 1)).thenReturn(false);

        tourService.handleNewBooking(bookingNotificationDto);

        verify(tourBookingsRepository, times(1)).save(any(TourBookings.class));
    }

    @Test
    void testPreventDuplicateBookings() {
        BookingNotificationDTO bookingNotificationDto = new BookingNotificationDTO();
        bookingNotificationDto.setTourId(1);
        bookingNotificationDto.setBookingId(1);

        when(tourBookingsRepository.existsByTourIdAndBookingId(1, 1)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            tourService.handleNewBooking(bookingNotificationDto);
        });

        assertEquals("Booking already exists for the given tour.", exception.getMessage());
        verify(tourBookingsRepository, never()).save(any(TourBookings.class));
    }
    @Test
    void testSelfAssignToTour_Success() {
        int tourId = 1;
        String emailId = "guide@example.com";

        when(restTemplate.getForObject("http://USER-MANAGEMENT-SERVICE/users/" + emailId + "/isTourGuide", Boolean.class))
                .thenReturn(true);
        Tour tour = new Tour();
        tour.setTourId(tourId);
        tour.setEmailId(null);
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tour));
        when(tourRepository.save(any(Tour.class))).thenAnswer(invocation -> invocation.getArgument(0));

        tourService.selfAssignToTour(tourId, emailId);

        verify(tourRepository, times(1)).findById(tourId);
        verify(tourRepository, times(1)).save(tour);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Boolean.class));
        assertEquals(emailId, tour.getEmailId());
    }

    @Test
    void testSelfAssignToTour_TourNotFound() {
        int invalidTourId = 6969;
        String emailId = "guide@example.com";

        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(true);
        when(tourRepository.findById(invalidTourId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            tourService.selfAssignToTour(invalidTourId, emailId);
        });

        assertEquals("Tour not found with ID: " + invalidTourId, exception.getMessage());
        verify(tourRepository, times(1)).findById(invalidTourId);
    }

    @Test
    void testSelfAssignToTour_InvalidEmail() {
        int tourId = 1;
        String emailId = "";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            tourService.selfAssignToTour(tourId, emailId);
        });

        assertEquals("Invalid emailId: " + emailId, exception.getMessage());

        verify(restTemplate, never()).getForObject(anyString(), any());
        verify(tourRepository, never()).findById(anyInt());
    }

    @Test
    void testSelfAssignToTour_NonTourGuide() {
        int tourId = 1;
        String emailId = "nonGuide@example.com";

        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            tourService.selfAssignToTour(tourId, emailId);
        });

        assertEquals("Only tour guides can assign themselves to a tour.", exception.getMessage());
        verify(tourRepository, never()).findById(anyInt());
    }

    @Test
    void testSelfAssignToTour_TourAlreadyAssigned() {
        int tourId = 1;
        String emailId = "anotherGuide@example.com";

        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(true); // Valid guide

        Tour tour = new Tour();
        tour.setTourId(tourId);
        tour.setEmailId("existingGuide@example.com");
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tour));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            tourService.selfAssignToTour(tourId, emailId);
        });

        assertEquals("Tour already has a tour guide assigned.", exception.getMessage());
        verify(tourRepository, times(1)).findById(tourId);
        verify(tourRepository, never()).save(any());
    }
    @Test
    void testSelfDeassignFromTour_Success() {
        int tourId = 1;
        String emailId = "guide@example.com";

        when(restTemplate.getForObject("http://USER-MANAGEMENT-SERVICE/users/" + emailId + "/isTourGuide", Boolean.class))
                .thenReturn(true);
        Tour tour = new Tour();
        tour.setTourId(tourId);
        tour.setEmailId(emailId);

        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tour));
        when(tourRepository.save(any(Tour.class))).thenAnswer(invocation -> invocation.getArgument(0));

        tourService.selfDeassignFromTour(tourId, emailId);

        verify(tourRepository, times(1)).save(tour);
        assertNull(tour.getEmailId(), "Tour guide should be deassigned.");
    }

    @Test
    void testGetAvailableTours() {
        List<Tour> availableTours = new ArrayList<>();
        availableTours.add(new Tour());
        when(tourRepository.findAvailableTours()).thenReturn(availableTours);

        List<Tour> result = tourService.getAvailableTours();

        assertEquals(1, result.size(), "Available tours should match");
        verify(tourRepository, times(1)).findAvailableTours();
    }

    @Test
    void testUpdateParticipantCount_OptimisticLocking() {
        Tour tour = new Tour();
        tour.setTourId(1);
        tour.setParticipantCount(10);
        tour.setMaxCapacity(20);
        tour.setVersion(1);
        when(tourRepository.findById(1)).thenReturn(Optional.of(tour));
        when(tourRepository.save(any(Tour.class))).thenThrow(new OptimisticLockException("Optimistic locking failure"));

        // Call the service method and assert the exception
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            tourService.updateParticipantCount(1, 5); // Attempt to add 5 participants
        });

        assertEquals("Concurrent booking. Try again.", exception.getMessage(),
                "The exception message should indicate a concurrency issue.");
        verify(tourRepository, times(1)).findById(1); // Ensure findById was called
        verify(tourRepository, times(1)).save(tour);  // Ensure save was attempted
    }

    @Test
    void testConcurrentBookings() throws InterruptedException {
        Tour tour = new Tour();
        tour.setTourId(1);
        tour.setParticipantCount(10);
        tour.setMaxCapacity(11); // Only 1 spot left
        tour.setVersion(1);

        // mocking repository behavior
        when(tourRepository.findById(1)).thenReturn(Optional.of(tour));
        when(tourRepository.save(any(Tour.class))).thenAnswer(invocation -> {
            // save logic with optimistic locking
            Tour savedTour = invocation.getArgument(0);
            if (savedTour.getParticipantCount() >= savedTour.getMaxCapacity()) {
                throw new OptimisticLockException("Simulated optimistic locking failure");
            }
            savedTour.setVersion(savedTour.getVersion() + 1);
            savedTour.setParticipantCount(savedTour.getParticipantCount() + 1);
            return savedTour;
        });

        // using a CountDownLatch to coordinate thread execution
        CountDownLatch latch = new CountDownLatch(2);

        Runnable bookingTask = () -> {
            try {
                tourService.updateParticipantCount(1, 1); // Attempt to book 1 spot
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName() + " failed: " + e.getMessage());
            } finally {
                latch.countDown(); // Signal task completion
            }
        };

        // executing tasks concurrently
        Thread user1 = new Thread(bookingTask, "User1");
        Thread user2 = new Thread(bookingTask, "User2");

        user1.start();
        user2.start();

        latch.await(); // Wait for both threads to finish

        verify(tourRepository, times(2)).findById(1); // Both threads tried to fetch the tour
        verify(tourRepository, atLeastOnce()).save(any(Tour.class)); // Save should be called at least once

        // Check that the participant count does not exceed capacity
        assertTrue(tour.getParticipantCount() <= tour.getMaxCapacity(),
                "Participant count exceeded max capacity");
    }

}
