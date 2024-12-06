package com.example.AuthenticationService.controller;

import com.example.AuthenticationService.dto.UserDTO;
import com.example.AuthenticationService.entity.UserEntity;
import com.example.AuthenticationService.service.AuthenticationService;
import com.example.AuthenticationService.service.JwtService;
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
    private final RestTemplate restTemplate;

    @Autowired
    public AuthenticationServiceController(AuthenticationService authenticationService,
                                           JwtService jwtService,
                                           RestTemplate restTemplate) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/grantcode")
    public String grantCode(@RequestParam(value = "code") String code) {
        try {
            Map<String, Object> authResponse = authenticationService.authenticationHandler(code);

            String jwtToken = (String) authResponse.get("jwtToken");
            UserDTO userDto = (UserDTO) authResponse.get("userDto");


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            headers.set("Authorization", "Bearer " + jwtToken);


            HttpEntity<UserDTO> requestEntity = new HttpEntity<>(userDto, headers);


            String userManagementUrl = "http://user-management-service:8084/users/createuser";

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<UserDTO> userManagementResponse = restTemplate.exchange(
                    userManagementUrl,
                    HttpMethod.POST,
                    requestEntity,
                    UserDTO.class
            );


            if (userManagementResponse.getStatusCode().is2xxSuccessful()) {
                return jwtToken;
            } else {
                throw new RuntimeException("Failed to create user in UserManagement service");
            }
        } catch (RestClientException e) {
            throw new RuntimeException("Error occurred during grant code processing: " + e.getMessage());
        }
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken() {
        try {
            Map<String, String> response = authenticationService.handleTokenRefresh();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @PostMapping("/refreshJWT")
    public ResponseEntity<?> refreshJwtToken() {
        try {
            Map<String, String> response = authenticationService.handleJWTTokenRefresh();
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
            authenticationService.removeUserByEmail(email);

            return ResponseEntity.ok("User with email " + email + " has been removed successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/ping")
    public String ping() {
        return "Pong";
    }

}
