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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/tour-management")
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

    @PostMapping("/notify")
    public ResponseEntity<String> notifyTourManagement(@RequestBody Booking booking) {
        // Prepare the BookingNotificationDto
        //this is the notification that is sent to the tour management service
        //I am sending only the booking id and the tour id
        BookingNotificationDTO notificationDto = new BookingNotificationDTO();
        notificationDto.setBookingId(booking.getBookingId());
        notificationDto.setTourId(booking.getTourId());

        // Defining the URL to the tourmanagementservice endpoint
        String url = "http://tourmanagementservice/tour-bookings/add-booking";

        // Creating the HTTP request entity
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<BookingNotificationDTO> request = new HttpEntity<>(notificationDto, headers);

        // Sending POST request using RestTemplate
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        return ResponseEntity.ok("Notification sent to tour management service. Response: " + response.getBody());
    }
}

