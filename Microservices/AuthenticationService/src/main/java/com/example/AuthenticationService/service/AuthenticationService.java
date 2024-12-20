package com.example.AuthenticationService.service;

import com.example.AuthenticationService.Objects.oAuthUser;
import com.example.AuthenticationService.Objects.oAuthResponse;
import com.example.AuthenticationService.dto.UserDTO;
import com.example.AuthenticationService.entity.UserEntity;
import com.example.AuthenticationService.exceptions.AuthenticationServiceExceptions;
import com.example.AuthenticationService.exceptions.JwtServiceExceptions;
import com.example.AuthenticationService.repository.UserRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;


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
        return userDto;
    }

    public UserEntity checkIfUserExistsInDB(String email) {
        return userRepository.findByEmail(email)
                .map(user -> {
                    checkIfRefreshTokenIsExpired(email);
                    return user; // Return the existing user
                })
                .orElseGet(() -> createNewUserInDB(email));
    }

    private UserEntity createNewUserInDB(String email) {
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setRefreshToken(UUID.randomUUID().toString());
        user.setRefreshTokenExpiry(LocalDateTime.now().plusDays(7));
        return userRepository.save(user);
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

    public void removeUserByEmail(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            userRepository.delete(user.get());
        } else {
            throw new RuntimeException("User with email " + email + " not found.");
        }
    }

    public Map<String, String> handleTokenRefresh() {
        HttpServletRequest request = getCurrentHttpRequest();
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header.");
        }

        String jwtToken = authHeader.substring(7);

        String email;
        String newJwtToken = null;

        try {
            Claims claims = jwtService.validateToken(jwtToken);
            email = claims.getSubject();
        } catch (JwtServiceExceptions.TokenExpiredException e) {
            Claims claims = jwtService.validateToken(jwtToken);
            email = claims.getSubject();

            newJwtToken = jwtService.generateToken(email);
        } catch (JwtServiceExceptions.InvalidTokenException e) {
            throw new RuntimeException("Invalid JWT token.");
        }

        UserEntity user = checkIfUserExistsInDB(email);

        if (user.getRefreshTokenExpiry().isBefore(LocalDateTime.now())) {
            generateNewRefreshToken(user);
        }

        if (newJwtToken == null) {
            newJwtToken = jwtToken;
        }

        Map<String, String> response = new HashMap<>();
        response.put("jwtToken", newJwtToken);
        response.put("refreshToken", user.getRefreshToken());
        return response;
    }

    public Map<String, String> handleJWTTokenRefresh() {
        HttpServletRequest request = getCurrentHttpRequest();
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header.");
        }

        String jwtToken = authHeader.substring(7);

        String user = jwtService.extractSubject(jwtToken);
        String newJwtToken = jwtService.generateToken(user);


        Map<String, String> response = new HashMap<>();
        response.put("jwtToken", newJwtToken);
        return response;
    }


    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new RuntimeException("Unable to fetch the current HTTP request.");
        }
        return attributes.getRequest();
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
