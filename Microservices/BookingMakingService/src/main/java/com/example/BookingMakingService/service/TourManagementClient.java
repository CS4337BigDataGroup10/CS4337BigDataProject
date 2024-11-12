package com.example.BookingMakingService.service;

import com.example.BookingMakingService.dto.BookingNotificationDTO;
import com.example.BookingMakingService.entity.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/tour-management")
public class TourManagementClient {

    private final RestTemplate restTemplate;

    @Autowired
    public TourManagementClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/notify")
    public ResponseEntity<String> notifyTourManagement(@RequestBody Booking booking) {
        // Prepare the BookingNotificationDto
        BookingNotificationDTO notificationDto = new BookingNotificationDTO();
        notificationDto.setBookingId(booking.getBookingId());
        notificationDto.setTourId(booking.getTour().getTourId());

        // Defining the URL to the tourmanagementservice endpoint
        String url = "http://tourmanagementservice/tour-bookings/add-booking";

        // Create the HTTP request entity
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<BookingNotificationDTO> request = new HttpEntity<>(notificationDto, headers);

        // Send POST request using RestTemplate
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        return ResponseEntity.ok("Notification sent to tour management service. Response: " + response.getBody());
    }
}

