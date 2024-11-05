package com.example.AuthenticationService.service;

import com.example.AuthenticationService.Objects.User;
import com.example.AuthenticationService.dto.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthenticationService {
    private String clientId;


    private String clientSecret;

    public AuthenticationService(@Value("${google.client.id}") String clientId,
                                 @Value("${google.client.secret}") String clientSecret) {

        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }


    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String REDIRECT_URI = "http://localhost:8080/grantcode";

    private String getOauthAccessTokenGoogle(String code) {
        //allows for HTTP request
        RestTemplate restTemplate = new RestTemplate();
        //allows me to set http headers for my request
        HttpHeaders httpHeaders = new HttpHeaders();
        //Set the media type
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //Map whic can hold multiple values for the using the same key
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        //Authorization code for oAuth
        params.add("code", code);
        //Set the response endpoint
        params.add("redirect_uri", "http://localhost:8081/grantcode");
        //A unique identifier for your application
        params.add("client_id", clientId);
        //A secret unique identifier for your application
        params.add("client_secret", clientSecret);
        //This defines access permissions to profile
        params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile");
        //This defines access permissions to email
        params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email");
        params.add("scope", "openid");
        //Indicates we are using the Authentication flow
        params.add("grant_type", "authorization_code");

        //Combines body and the header
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);

        String url = "https://oauth2.googleapis.com/token";

        String response = restTemplate.postForObject(url, requestEntity, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.get("access_token").asText();
        }catch (JsonProcessingException e) {
          throw new RuntimeException(e);
        }
    }


    private User getProfileDetailsGoogle(String accessToken) {
        //allows for HTTP request
        RestTemplate restTemplate = new RestTemplate();
        //allows me to set http headers for my request
        HttpHeaders httpHeaders = new HttpHeaders();
        //adds the access token to the header of HTTP request
        httpHeaders.setBearerAuth(accessToken);
        //wrapper for HTTP object : Header Params Body
        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);
        //Set URL to the Google oAuth Server
        String url = "https://www.googleapis.com/oauth2/v2/userinfo";
        //Create a Wrapper containing the Response
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        //Setting request response to a string
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        //Map the response to the User Object
        try {
            User user = objectMapper.readValue(responseBody, User.class);
            return user;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public User returnUserInfo(String code) {
        String accessToken = getOauthAccessTokenGoogle(code);
        User userDetails = getProfileDetailsGoogle(accessToken);
        return userDetails;
    }
}
