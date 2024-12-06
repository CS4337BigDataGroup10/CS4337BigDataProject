package com.example.TourManagementService.contoller;

import com.example.TourManagementService.dto.BookingNotificationDTO;
import com.example.TourManagementService.entity.Tour;
import com.example.TourManagementService.service.TourService;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/tours")
@Tag(name = "Tour Management", description = "APIs for managing tours")
public class TourManagementServiceController {

    private final TourService tourService;

    public TourManagementServiceController(TourService tourService) {
        this.tourService = tourService;
    }

    @Operation(
            summary = "Get available tours",
            description = "Retrieve a list of tours that are currently available",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of available tours",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Tour.class)))
            }
    )
    @GetMapping("/available")
    public ResponseEntity<List<Tour>> getAvailableTours() {
        List<Tour> availableTours = tourService.getAvailableTours();
        return ResponseEntity.ok(availableTours);
    }

    @Operation(
            summary = "Get tour details",
            description = "Retrieve details for a specific tour by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved tour details",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Tour.class))),
                    @ApiResponse(responseCode = "404", description = "Tour not found")
            }
    )
    @GetMapping("/{tourId}")
    public ResponseEntity<Tour> getTourDetails(@PathVariable int tourId) {
        Tour tour = tourService.getTourById(tourId);
        return ResponseEntity.ok(tour);
    }

    @Operation(
            summary = "Update participant count",
            description = "Update the participant count for a specific tour by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Participant count updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid participant count")
            }
    )
    @PutMapping("/{tourId}/update-participant-count")
    public ResponseEntity<String> updateParticipantCount(@PathVariable int tourId, @RequestParam int size) {
        try {
            tourService.updateParticipantCount(tourId, size);
            return ResponseEntity.ok("Participant count updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Self-assign to a tour",
            description = "Assign yourself to a tour by providing your email ID",
            responses = {@ApiResponse(responseCode = "200", description = "Successfully assigned to the tour")}
    )
    @PostMapping("/{tourId}/self-assign")
    public void selfAssignToTour(@PathVariable int tourId, @RequestParam String emailId) {
        tourService.selfAssignToTour(tourId, emailId);
    }

    @Operation(
            summary = "Self-deassign from a tour",
            description = "Remove yourself from a tour by providing your email ID",
            responses = {@ApiResponse(responseCode = "200", description = "Successfully deassigned from the tour")}
    )
    @PostMapping("/{tourId}/self-deassign")
    public void selfDeassignFromTour(@PathVariable int tourId, @RequestParam String emailId) {
        tourService.selfDeassignFromTour(tourId, emailId);
    }

    @Operation(
            summary = "Create a new tour",
            description = "Create a new tour by providing the tour details",
            requestBody = @RequestBody(description = "Tour details", content = @Content(schema = @Schema(implementation = Tour.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tour created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Tour.class)))
            }
    )
    @PostMapping("/create")
    public ResponseEntity<Tour> createTour(@RequestBody Tour tour) {
        Tour savedTour = tourService.createTour(tour);
        return ResponseEntity.ok(savedTour);
    }

    @Operation(
            summary = "Add a booking to a tour",
            description = "Add a new booking to a specific tour",
            requestBody = @RequestBody(description = "Booking details", content = @Content(schema = @Schema(implementation = BookingNotificationDTO.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Booking added successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid booking details")
            }
    )
    @PutMapping("/{tourId}/addBooking")
    public ResponseEntity<String> addBooking(@RequestBody BookingNotificationDTO bookingNotificationDto) {
        try {
            tourService.addBookingtoTour(bookingNotificationDto);
            return ResponseEntity.ok("Booking added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Remove a booking from a tour",
            description = "Remove an existing booking from a specific tour",
            requestBody = @RequestBody(description = "Booking details", content = @Content(schema = @Schema(implementation = BookingNotificationDTO.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Booking removed successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid booking details")
            }
    )
    @PutMapping("/{tourId}/removeBooking")
    public ResponseEntity<String> removeBooking(@RequestBody BookingNotificationDTO bookingNotificationDto) {
        try {
            tourService.removeBooking(bookingNotificationDto.getTourId(), bookingNotificationDto.getBookingId());
            return ResponseEntity.ok("Booking removed successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}