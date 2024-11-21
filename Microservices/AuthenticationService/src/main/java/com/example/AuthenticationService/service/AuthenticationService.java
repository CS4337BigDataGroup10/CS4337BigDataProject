package com.example.AuthenticationService.service;

import com.example.AuthenticationService.Objects.oAuthUser;
import com.example.AuthenticationService.Objects.oAuthResponse;
import com.example.AuthenticationService.dto.UserDTO;
import com.example.AuthenticationService.entity.UserEntity;
import com.example.AuthenticationService.exceptions.AuthenticationServiceExceptions;
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
import java.util.HashMap;
import java.util.Map;
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

    public Map<String, Object> authenticationHandler(String code) {
        oAuthResponse oAuthResponse = codeExchangeFromOauth(code);
        if (oAuthResponse == null) {
            throw new AuthenticationServiceExceptions.AuthenticationFailedException(
                    "Failed to obtain access token from OAuth. Authorization code may be invalid or expired."
            );
        }

        String oAuthToken = oAuthResponse.getAccess_token();
        oAuthUser userInfo = getProfileDetailsGoogle(oAuthToken);
        checkIfUserExistsInDB(userInfo.getEmail());
        String jwtToken = jwtService.generateToken(userInfo.getEmail());

        UserDTO userDto = createUserDtoForUserManagementService(userInfo);
        Map<String, Object> response = new HashMap<>();
        response.put("jwtToken", jwtToken);
        response.put("userDto", userDto);

        return response;
    }

    private UserDTO createUserDtoForUserManagementService(oAuthUser OauthUser) {
        UserDTO userDto = new UserDTO();
        userDto.setEmail(OauthUser.getEmail());
        userDto.setGivenName(OauthUser.getGiven_name());
        userDto.setFamilyName(OauthUser.getFamily_name());
        userDto.setProfilePicture(OauthUser.getPicture());
        userDto.setRole("Customer");
        return userDto;
    }

    public UserEntity checkIfUserExistsInDB(String email) {
        return userRepository.findByEmail(email)
                .map(user -> {
                    // If user exists, check if the refresh token is expired
                    checkIfRefreshTokenIsExpired(email);
                    return user; // Return the existing user
                })
                .orElseGet(() -> createNewUserInDB(email)); // If user doesn't exist, create a new user
    }

    private UserEntity createNewUserInDB(String email) {
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setRefreshToken(UUID.randomUUID().toString());
        user.setRefreshTokenExpiry(LocalDateTime.now().plusDays(7));
        return userRepository.save(user); // Return the saved user
    }

    public void checkIfRefreshTokenIsExpired(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationServiceExceptions.UserNotFoundException(
                        "User with email '" + email + "' not found in the database."
                ));

        if (user.getRefreshTokenExpiry().isBefore(LocalDateTime.now())) {
            generateNewRefreshToken(user);
        }
    }

    private void generateNewRefreshToken(UserEntity user) {
        String newRefreshToken = UUID.randomUUID().toString();
        LocalDateTime newExpiry = LocalDateTime.now().plusDays(7);

        user.setRefreshToken(newRefreshToken);
        user.setRefreshTokenExpiry(newExpiry);
        userRepository.save(user);
        System.out.println("Refresh token has been updated.");
    }

    public oAuthResponse codeExchangeFromOauth(String code) {
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
                throw new AuthenticationServiceExceptions.OAuthTokenExchangeException(
                        "Failed to exchange authorization code for access token. HTTP Status: " + response.getStatusCode()
                );
            }
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return objectMapper.treeToValue(jsonNode, oAuthResponse.class);
        } catch (Exception e) {
            throw new AuthenticationServiceExceptions.OAuthTokenExchangeException("Error during token exchange", e);
        }
    }

    public oAuthUser getProfileDetailsGoogle(String accessToken) {
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
            throw new AuthenticationServiceExceptions.UserProfileRetrievalException(
                    "Failed to retrieve user profile details from Google", e
            );
        }
    }
}
