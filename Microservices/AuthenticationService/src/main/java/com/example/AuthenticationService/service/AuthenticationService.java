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
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AuthenticationService {

    private final String clientId;
    private final String clientSecret;
    private final UserRepository userRepository;
    private final String redirectUrl;

    @Autowired
    public AuthenticationService(
            @Value("${google.client.id}") String clientId,
            @Value("${google.client.secret}") String clientSecret,
            @Value("${redirect.url}") String redirectUrl,
            UserRepository userRepository) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.userRepository = userRepository;
        this.redirectUrl = redirectUrl;
    }

    public UserDTO authenticationHandler(String code) {
        // Exchange Auth code for access token
        oAuthResponse oAuthResponse = codeExchangeFromOauth(code);
        if (oAuthResponse == null) {
            throw new RuntimeException("Failed to obtain access token from OAuth.");
        }

        // Get the token from the response
        String oAuthToken = oAuthResponse.getAccess_token();
        // Get the user details from the access token
        oAuthUser userInfo = getProfileDetailsGoogle(oAuthToken);

        // Find user in database or create a new one
        UserEntity user = userRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> createNewUserUsingOauthToken(userInfo));

        // Create a UserDTO to return
        UserDTO userDto = new UserDTO();
        userDto.setEmail(user.getEmail());
        // Add other fields to UserDTO as necessary
        return userDto;
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
        params.add("redirect_uri", this.redirectUrl );
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
