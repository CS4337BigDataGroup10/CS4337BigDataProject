package com.example.BookingMakingService.service;

import com.example.BookingMakingService.dto.BookingNotificationDTO;
import com.example.BookingMakingService.entity.Booking;
import com.example.BookingMakingService.entity.Tour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;


import java.util.List;

@Service
public class TourManagementClient {

    private final RestTemplate restTemplate;

    @Autowired
    public TourManagementClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //may need to change the url to the correct name when we implement eureka server.
    public List<Tour> getNonFullTours(String authorization) {
       try{
        String url = "http://tour-management-service/tours/available"; // This is the end point of the tour management service to see what tours are available. It checks any tour less than 20
        System.out.println("Connecting to URL: " + url);
        System.out.println("Token = "+ authorization);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorization);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<List<Tour>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Tour>>() {}
        );
        System.out.println(response.getBody());
        System.out.println("Response Status: " + response.getStatusCode());
        return response.getBody();
        } catch (RestClientException e) {
        // Log and handle client/server errors
        System.err.println("Error during REST call to Tour Management Service: " + e.getMessage());
        throw new IllegalStateException("Could not fetch available tours", e);
     }
    }

    // Notify the tour management service about a new booking
    public ResponseEntity<String> notifyTourManagement(Booking booking,String token) {
        BookingNotificationDTO notificationDto = createNotificationDTO(booking);
        return sendNotification(notificationDto,token);
    }

    // Notify the tour management service about a booking cancellation
    public ResponseEntity<String> notifyCancellation(Booking booking,String token) {
        BookingNotificationDTO notificationDto = createNotificationDTO(booking);
        return sendNotification(notificationDto,token); // true for booking removal
    }

    // Helper method to create a BookingNotificationDTO from a Booking
    private BookingNotificationDTO createNotificationDTO(Booking booking) {
        BookingNotificationDTO notificationDto = new BookingNotificationDTO();
        notificationDto.setBookingId(booking.getBookingId());
        notificationDto.setTourId(booking.getTourId());
        notificationDto.setiscancelled(booking.isCancelled());
        notificationDto.setEmail(booking.getEmailId());
        return notificationDto;
    }

    // Send notification for either booking or cancellation
    public ResponseEntity<String> sendNotification(BookingNotificationDTO notificationDto,String token) {
        String url = notificationDto.getIsIscancelled()
                ? "http://tour-management-service/tours/" + notificationDto.getTourId() + "/removeBooking"
                : "http://tour-management-service/tours/" + notificationDto.getTourId() + "/addBooking";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(token); // Add Authorization header
        HttpEntity<BookingNotificationDTO> request = new HttpEntity<>(notificationDto, headers);


        // Send the request
        try {
            return restTemplate.exchange(url, HttpMethod.PUT, request, String.class); // Return the response from the TourManagementService
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error communicating with Tour Management Service: " + e.getMessage());
        }
    }
}
