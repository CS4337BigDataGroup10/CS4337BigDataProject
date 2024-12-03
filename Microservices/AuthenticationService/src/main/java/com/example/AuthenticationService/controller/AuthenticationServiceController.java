package com.example.AuthenticationService.controller;

import com.example.AuthenticationService.dto.UserDTO;
import com.example.AuthenticationService.entity.UserEntity;
import com.example.AuthenticationService.exceptions.AuthenticationServiceExceptions;
import com.example.AuthenticationService.exceptions.JwtServiceExceptions;
import com.example.AuthenticationService.service.AuthenticationService;
import com.example.AuthenticationService.service.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
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

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshJwtToken(@RequestHeader("Authorization") String authHeader) {
        try {
            Map<String, String> response = authenticationService.handleTokenRefresh(authHeader);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }



}
