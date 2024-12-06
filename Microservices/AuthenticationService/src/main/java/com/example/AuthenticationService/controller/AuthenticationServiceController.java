package com.example.AuthenticationService.controller;

import com.example.AuthenticationService.dto.UserDTO;
import com.example.AuthenticationService.entity.UserEntity;
import com.example.AuthenticationService.service.AuthenticationService;
import com.example.AuthenticationService.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Management", description = "APIs for handling authentication, token validation, and user details")
public class AuthenticationServiceController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final RestTemplate restTemplate; // Add RestTemplate as a field

    @Autowired
    public AuthenticationServiceController(AuthenticationService authenticationService,
                                           JwtService jwtService,
                                           RestTemplate restTemplate) { // Autowire RestTemplate
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.restTemplate = restTemplate; // Initialize the RestTemplate
    }

    @GetMapping("/grantcode")
    public String grantCode(@RequestParam(value = "code") String code) {
        try {
            // Call authenticationHandler to get the JWT and UserDTO
            Map<String, Object> authResponse = authenticationService.authenticationHandler(code);

            // Extract JWT and UserDTO
            String jwtToken = (String) authResponse.get("jwtToken");
            UserDTO userDto = (UserDTO) authResponse.get("userDto");

            // Print the JWT token for debugging purposes
            System.out.println("JWT Token: " + jwtToken);
            System.out.println("UserDTO1: " + userDto);

            // Send the UserDTO to the UserManagementService
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Set the Authorization header with the JWT token
            headers.set("Authorization", "Bearer " + jwtToken);
            System.out.println("Authorization Header: " + headers.get("Authorization"));

            // Wrap the DTO in the HttpEntity with the headers
            HttpEntity<UserDTO> requestEntity = new HttpEntity<>(userDto, headers);
            System.out.println("ENTITY MADE");
            System.out.println("UserDTO2: " + userDto);

            // Use Eureka to resolve the user-management-service's instance dynamically
            String userManagementUrl = "http://user-management-service:8084/users/createuser"; // Use the service name registered with Eureka

            // Sending the POST request using RestTemplate
            RestTemplate restTemplate = new RestTemplate(); // Ensure RestTemplate is properly configured with load balancing and Eureka integration
            System.out.println("PRE SENT");

            // Execute the POST request and capture the response
            ResponseEntity<String> userManagementResponse = restTemplate.exchange(
                    userManagementUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class // Adjust the response type if needed
            );
            System.out.println("UserDTO3: "+ requestEntity.getBody());

            System.out.println("SENT");
            System.out.println("UserDTO4: " + userDto);

            // Check the response and handle accordingly
            if (userManagementResponse.getStatusCode().is2xxSuccessful()) {
                // If everything is good, return the JWT token
                return jwtToken;
            } else {
                throw new RuntimeException("Failed to create user in UserManagement service");
            }
        } catch (RestClientException e) {
            // Handle client-side errors (e.g., connection issues)
            throw new RuntimeException("Error occurred during grant code processing: " + e.getMessage());
        }
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refreshJwtToken() {
        try {
            Map<String, String> response = authenticationService.handleTokenRefresh();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@RequestParam("email") String email) {
        try {
            UserEntity user = authenticationService.checkIfUserExistsInDB(email);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("email") String email) {
        try {
            // Attempt to remove the user by email
            authenticationService.removeUserByEmail(email);

            // Return a success response
            return ResponseEntity.ok("User with email " + email + " has been removed successfully.");
        } catch (RuntimeException e) {
            // Handle exception and return an error response
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
