package com.example.BookingMakingService;


import com.example.BookingMakingService.dto.BookingNotificationDTO;
import com.example.BookingMakingService.entity.Booking;
import com.example.BookingMakingService.entity.Tour;
import com.example.BookingMakingService.service.TourManagementClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Ensures Mockito handles annotations
public class TourManagementClientTest {
    @Spy
    @InjectMocks
    private TourManagementClient tourManagementClient;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void getNonFullTours_shouldReturnListOfTours() {
        Tour tour1 = new Tour();
        tour1.setTourId(1);
        Tour tour2 = new Tour();
        tour2.setTourId(2);
        String url = "http://tour-management-service/tours/available";
        List<Tour> expectedTours = Arrays.asList(tour1,tour2);
        ResponseEntity<List<Tour>> responseEntity = new ResponseEntity<>(expectedTours, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(
                Mockito.eq(url),
                Mockito.eq(HttpMethod.GET),
                Mockito.isNull(),
                Mockito.<ParameterizedTypeReference<List<Tour>>>any()
        )).thenReturn(responseEntity);

        List<Tour> actualTours = tourManagementClient.getNonFullTours();

        Assertions.assertEquals(expectedTours, actualTours);
        Mockito.verify(restTemplate).exchange(
                Mockito.eq(url),
                Mockito.eq(HttpMethod.GET),
                Mockito.isNull(),
                Mockito.<ParameterizedTypeReference<List<Tour>>>any()
        );
    }

//    @Test
//    void tetsnotifyTourManagement() {
//        // Arrange
//        Booking booking = new Booking();
//        booking.setBookingId(1);
//        booking.setCancelled(false);
//        booking.setEmailId("example@test.com");
//        BookingNotificationDTO notificationDto = new BookingNotificationDTO(1, 201,false,"test@test.test");
//        Mockito.doReturn(ResponseEntity.ok("Success"))
//                .when(tourManagementClient)
//                .sendNotification(Mockito.any(BookingNotificationDTO.class));
//
//        // Act
//        ResponseEntity<String> response = tourManagementClient.notifyTourManagement(booking);
//
//        // Assert
//        Assertions.assertEquals("Success", response.getBody());
//        Mockito.verify(tourManagementClient).sendNotification(
//                Mockito.any(BookingNotificationDTO.class));
//    }


}