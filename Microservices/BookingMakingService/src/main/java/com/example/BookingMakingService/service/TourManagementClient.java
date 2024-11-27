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

import java.util.List;

@Service
public class TourManagementClient {

    private final RestTemplate restTemplate;

    @Autowired
    public TourManagementClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    //may need to change the url to the correct name when we implement eureka server.
    public List<Tour> getNonFullTours() {
        String url = "http://tour-management-service/api/tours/available"; // This is the end point of the tour management service to see what tours are available. It checks any tour less than 20
        ResponseEntity<List<Tour>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Tour>>() {}
        );
        return response.getBody();
    }

    // Notify the tour management service about a new booking
    public ResponseEntity<String> notifyTourManagement(Booking booking) {
        BookingNotificationDTO notificationDto = createNotificationDTO(booking);
        return sendNotification(notificationDto, false); // false for booking addition
    }

    // Notify the tour management service about a booking cancellation
    public ResponseEntity<String> notifyCancellation(Booking booking) {
        BookingNotificationDTO notificationDto = createNotificationDTO(booking);
        return sendNotification(notificationDto, true); // true for booking removal
    }

    // Helper method to create a BookingNotificationDTO from a Booking
    private BookingNotificationDTO createNotificationDTO(Booking booking) {
        BookingNotificationDTO notificationDto = new BookingNotificationDTO();
        notificationDto.setBookingId(booking.getBookingId());
        notificationDto.setTourId(booking.getTourId());
        return notificationDto;
    }

    // Send notification for either booking or cancellation
    private ResponseEntity<String> sendNotification(BookingNotificationDTO notificationDto, boolean isCancellation) {
        String url = isCancellation
                ? "http://tour-management-service/api/tours/" + notificationDto.getTourId() + "/removeBooking"
                : "http://tour-management-service/api/tours/" + notificationDto.getTourId() + "/addBooking";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<BookingNotificationDTO> request = new HttpEntity<>(notificationDto, headers);

        // Send the request
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
            return response; // Return the response from the TourManagementService
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error communicating with Tour Management Service: " + e.getMessage());
        }
    }
}
