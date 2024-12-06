package com.example.AuthenticationService.controller;

import com.example.AuthenticationService.dto.UserDTO;
import com.example.AuthenticationService.entity.UserEntity;
import com.example.AuthenticationService.service.AuthenticationService;
import com.example.AuthenticationService.service.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Management", description = "APIs for handling authentication, token validation, and user details")
public class AuthenticationServiceController {

    @Autowired
    private final AuthenticationService authenticationService;

    @Autowired
    private final JwtService jwtService;

    public AuthenticationServiceController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @GetMapping("/grantcode")
    public ResponseEntity<String> grantCode(@RequestParam(value = "code") String code) {
        try {
            if (code == null || code.isEmpty()) {
                return ResponseEntity.badRequest().body("Authorization code is missing.");
            }
            Map<String, Object> authResponse = authenticationService.authenticationHandler(code);

            // Extract JWT and UserDTO
            String jwtToken = (String) authResponse.get("jwtToken");
            UserDTO userDto = (UserDTO) authResponse.get("userDto");


            // Send the UserDTO to the UserManagementService
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

            HttpEntity<UserDTO> requestEntity = new HttpEntity<>(userDto, headers);
            RestTemplate restTemplate = new RestTemplate();

            // ResponseEntity<?> userManagementResponse = restTemplate.exchange(
            //         userManagementMicroservices,
            //         HttpMethod.POST,
            //         requestEntity,
            //         Object.class // Replace with the expected response type
            // );

            return ResponseEntity.ok(jwtToken);
        } catch (Exception e) {
            // Handle unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the grant code: " + e.getMessage());
        }
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refreshJwtToken(@RequestHeader("Authorization") String authHeader) {
        try {
            Map<String, String> response = authenticationService.handleTokenRefresh(authHeader);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
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
}
