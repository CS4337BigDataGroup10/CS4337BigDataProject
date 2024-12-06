package com.example.BookingMakingService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

@Service
public class JwtServiceClient {

    private final RestTemplate restTemplate;

    @Value("${jwt.service.url}")
    private String jwtServiceUrl;

    @Autowired
    public JwtServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getEmailFromJwtService(String authorization) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorization);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    jwtServiceUrl,
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );
            System.out.println(response.getBody());
            return response.getBody();

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch email from JWT service: " + e.getMessage());
        }
    }

}
