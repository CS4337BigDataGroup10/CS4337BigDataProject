//package com.example.TourManagementService;
//
//import com.example.TourManagementService.entity.Tour;
//import com.example.TourManagementService.repository.TourRepository;
//import com.example.TourManagementService.service.TourScheduler;
//import com.example.TourManagementService.service.TourService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@SpringBootTest
//@Transactional
//public class TourSchedulerTest {
//
//    @Autowired
//    private TourScheduler tourScheduler;
//
//    @Autowired
//    private TourRepository tourRepository;
//
//    @Autowired
//    private TourService tourService;
//
//    @Test
//    void testCreateDailyTours() {
//        tourRepository.deleteAll();
//
//        tourScheduler.createDailyTours();
//
//        // Verify that tours were created
//        List<Tour> tours = tourRepository.findAll();
//        Assertions.assertFalse(tours.isEmpty(), "Daily tours were not created!");
//        Assertions.assertEquals(9, tours.size(), "Incorrect number of daily tours created!");
//    }
//
//    @Test
//    void testNoDuplicateDailyTours() {
//        //Ensure no tours exist
//        tourRepository.deleteAll();
//
//        // Create daily tours twice
//        tourScheduler.createDailyTours();
//        tourScheduler.createDailyTours();
//
//        // Verify no duplicate tours were created
//        List<Tour> tours = tourRepository.findAll();
//        Assertions.assertEquals(9, tours.size(), "Duplicate daily tours were created!");
//    }

// THOSE TESTS HAVE WORKED WHEN TESTED BUT HAVE TO COMMENT THEM OUT DUE TO EUREKA NOT BEING SET UP YET AND CANT GET INFO IF USER IS A TOURGUIDE OR NOT

//    @Test
//    void testAssignTourGuideToScheduledTour() {
//
//        tourScheduler.createDailyTours();
//        List<Tour> tours = tourRepository.findAll();
//        Assertions.assertFalse(tours.isEmpty(), "No tours were created by the scheduler!");
//
//        Tour tour = tours.get(0);
//
//        String tourGuideEmail = "guide@example.com";
//
//        //Assign a tour guide to the tour
//        tourService.selfAssignToTour(tour.getTourId(), tourGuideEmail);
//
//        // Verify the tour guide assignment
//        Tour updatedTour = tourRepository.findById(tour.getTourId()).orElseThrow();
//        Assertions.assertEquals(tourGuideEmail, updatedTour.getEmailId(), "Tour guide assignment failed!");
//    }

//    @Test
//    void testTourGuideCannotBeAssignedTwice() {
//        //Create daily tours and assign a guide to the first tour
//        tourScheduler.createDailyTours();
//        List<Tour> tours = tourRepository.findAll();
//        Assertions.assertFalse(tours.isEmpty(), "No tours were created by the scheduler!");
//
//        Tour tour = tours.get(0);
//        String tourGuideEmail = "guide@example.com";
//        tourService.selfAssignToTour(tour.getTourId(), tourGuideEmail);
//
//        // Try to assign another guide and expect an exception
//        String anotherGuideEmail = "anotherguide@example.com";
//        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
//                tourService.selfAssignToTour(tour.getTourId(), anotherGuideEmail)
//        );
//        Assertions.assertEquals("Tour already has a tour guide assigned.", exception.getMessage());
//    }

//    @Test
//    void testCannotAssignNonTourGuide() {
//        tourScheduler.createDailyTours();
//        List<Tour> tours = tourRepository.findAll();
//        Assertions.assertFalse(tours.isEmpty(), "No tours were created by the scheduler!");
//
//        Tour tour = tours.get(0);
//        String nonTourGuideEmail = "notaguide@example.com"; // Invalid guide email
//
//        // Try to assign a non-tour guide and expect an exception
//        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
//                tourService.selfAssignToTour(tour.getTourId(), nonTourGuideEmail)
//        );
//        Assertions.assertEquals("Only tour guides can assign themselves to a tour.", exception.getMessage());
//    }

//    @Test
//    void testAssignTourGuideToNonexistentTour() {
//        // ry to assign a guide to a non-existent tour and expect an exception
//        int invalidTourId = 6969;
//        String tourGuideEmail = "guide@example.com";
//
//        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
//                tourService.selfAssignToTour(invalidTourId, tourGuideEmail)
//        );
//        Assertions.assertEquals("Tour not found with ID: " + invalidTourId, exception.getMessage());
//    }
//}
