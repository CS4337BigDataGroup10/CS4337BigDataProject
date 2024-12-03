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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
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

    @Operation(
            summary = "Grant code and retrieve JWT",
            description = "Accepts an authorization code, handles authentication, and returns a JWT token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "JWT token successfully generated"),
                    @ApiResponse(responseCode = "500", description = "Error during authentication processing")
            }
    )
    @GetMapping("/grantcode")
    public String grantCode(@RequestParam(value = "code") String code) {
        try {
            // Call authenticationHandler to get the JWT and UserDTO
            Map<String, Object> authResponse = authenticationService.authenticationHandler(code);

            // Extract JWT and UserDTO
            String jwtToken = (String) authResponse.get("jwtToken");
            UserDTO userDto = (UserDTO) authResponse.get("userDto");


            // Send the UserDTO to the UserManagementService
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

            HttpEntity<UserDTO> requestEntity = new HttpEntity<>(userDto, headers);
            RestTemplate restTemplate = new RestTemplate();

//            ResponseEntity<?> userManagementResponse = restTemplate.exchange(
//                    userManagementMicroservices,
//                    HttpMethod.POST,
//                    requestEntity,
//                    Object.class // Replace with the expected response type
//            );

            return jwtToken;
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Validate JWT token",
            description = "Validates a provided JWT token and checks its authenticity",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token is valid"),
                    @ApiResponse(responseCode = "401", description = "Token is invalid or expired")
            }
    )
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam("token") String token) {
        try {
            jwtService.validateToken(token);
            return ResponseEntity.ok("Token is valid.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Get user details by email",
            description = "Retrieves user details from the database using the provided email",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User details retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserEntity.class))),
                    @ApiResponse(responseCode = "404", description = "User not found in the database")
            }
    )
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
