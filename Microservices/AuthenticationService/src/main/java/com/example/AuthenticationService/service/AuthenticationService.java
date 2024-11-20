package com.example.AuthenticationService.service;

import com.example.AuthenticationService.Objects.oAuthUser;
import com.example.AuthenticationService.Objects.oAuthResponse;
import com.example.AuthenticationService.dto.UserDTO;
import com.example.AuthenticationService.entity.UserEntity;
import com.example.AuthenticationService.repository.UserRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthenticationService {

    private final String clientId;
    private final String clientSecret;
    private final UserRepository userRepository;
    private final String redirectUrl;
    private final JwtService jwtService;

    public AuthenticationService(
            @Value("${google.client.id}") String clientId,
            @Value("${google.client.secret}") String clientSecret,
            @Value("${redirect.url}") String redirectUrl,
            UserRepository userRepository,
            JwtService jwtService) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.userRepository = userRepository;
        this.redirectUrl = redirectUrl;
        this.jwtService = jwtService;
    }

    public UserDTO authenticationHandler(String code) {
        oAuthResponse oAuthResponse = codeExchangeFromOauth(code);
        if (oAuthResponse == null) {
            throw new RuntimeException("Failed to obtain access token from OAuth.");
        }

        String oAuthToken = oAuthResponse.getAccess_token();
        oAuthUser userInfo = getProfileDetailsGoogle(oAuthToken);

        UserEntity user = userRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> createNewUserUsingOauthToken(userInfo));
        UserDTO userDto = new UserDTO();

        String jwtToken = userDto.getJwtToken();
        boolean isJwtValid = false;
        try {
            if (jwtToken != null) {
                jwtService.validateToken(jwtToken);
                isJwtValid = true;
            }
        } catch (RuntimeException e) {
            refreshTokenLogic(user.getEmail());
        }

        if (!isJwtValid) {
            long oneHourInMillis = 60 * 60 * 1000;
            jwtToken = jwtService.generateToken(user.getEmail(), oneHourInMillis);
            userDto.setJwtToken(jwtToken);
        }

        userDto.setEmail(user.getEmail());
        userDto.setGivenName(userInfo.getGiven_name());
        userDto.setFamilyName(userInfo.getFamily_name());
        userDto.setProfilePicture(userInfo.getPicture());
        userDto.setRole("Customer");

        return userDto;
    }

    public String refreshUserToken(String email) {
        // Retrieve the user from the database
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the refresh token has expired
        if (user.getRefreshTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token has expired. User must reauthenticate.");
        }

        // Generate a new JWT token with a 1-hour expiry
        long oneHourInMillis = 60 * 60 * 1000;
        String newToken = jwtService.generateToken(email, oneHourInMillis);

        // Save the new token to the UserEntity
        user.setRefreshToken(newToken); // Assuming you want to save the JWT as the refresh token
        user.setRefreshTokenExpiry(LocalDateTime.now().plusHours(1)); // Update expiry
        userRepository.save(user); // Persist the changes

        System.out.println("Generated and saved new JWT token for user: " + email);
        return newToken;
    }


    public UserEntity getUserAuthDetails(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user;
    }

    private void refreshTokenLogic(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRefreshTokenExpiry().isBefore(LocalDateTime.now())) {
            String newRefreshToken = UUID.randomUUID().toString();
            LocalDateTime newExpiry = LocalDateTime.now().plusDays(7);

            user.setRefreshToken(newRefreshToken);
            user.setRefreshTokenExpiry(newExpiry);
            userRepository.save(user);

            System.out.println("Refresh token has been updated.");
        } else {
            System.out.println("Refresh token is still valid.");
        }
    }

    private UserEntity createNewUserUsingOauthToken(oAuthUser userInfo) {
        UserEntity user = new UserEntity();
        user.setEmail(userInfo.getEmail());
        user.setRefreshToken(UUID.randomUUID().toString());
        user.setRefreshTokenExpiry(LocalDateTime.now().plusDays(7));
        userRepository.save(user);
        return user;
    }

    private oAuthResponse codeExchangeFromOauth(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("redirect_uri", this.redirectUrl);
        params.add("client_id", this.clientId);
        params.add("client_secret", this.clientSecret);
        params.add("scope", "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email openid");
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        try {
            String url = "https://oauth2.googleapis.com/token";
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to exchange code for access token.");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return objectMapper.treeToValue(jsonNode, oAuthResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error exchanging code for access token", e);
        }
    }

    private oAuthUser getProfileDetailsGoogle(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

        String url = "https://www.googleapis.com/oauth2/v2/userinfo";

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.getBody(), oAuthUser.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing user profile details", e);
        }
    }
}
