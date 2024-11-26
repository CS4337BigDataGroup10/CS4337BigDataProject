package com.example.UserManagementService.service;

import com.example.UserManagementService.dto.UserDTO;
import com.example.UserManagementService.entity.UserEntity;
import com.example.UserManagementService.exceptions.UserManagementServiceExceptions;
import com.example.UserManagementService.repository.UserManagementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;




@Service
public class UserService {
    private final UserManagementRepository userRepository;
    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);



    @Autowired
    public UserService(UserManagementRepository userManagementRepository, RestTemplate restTemplate) {
        this.userRepository = userManagementRepository;
        this.restTemplate = restTemplate;
    }


    public UserEntity createUser(String givenName, String familyName, String email) {
        UserEntity user = new UserEntity();
        user.setGivenName(givenName);
        user.setFamilyName(familyName);
        user.setEmail(email);
        user.setIsTourGuide(false); // Default value

        return userRepository.save(user);
    }

    public UserEntity handleNewUserLogin(UserDTO userDTO) {
        // Log the incoming email for debugging
        logger.debug("Attempting to log in user with email: {}", userDTO.getEmail());

        // Check if the user already exists in the database
        Optional<UserEntity> existingUser = userRepository.findById(userDTO.getEmail());

        if (existingUser.isPresent()) {
            // If user exists, log that the user is found
            logger.debug("User already exists with email: {}", userDTO.getEmail());
            return existingUser.get(); // Return the existing user
        } else {
            // If not, create and save a new user
            logger.debug("No existing user found. Creating new user with email: {}", userDTO.getEmail());
            UserEntity newUser = new UserEntity(userDTO.getEmail(), userDTO.getGivenName(), userDTO.getFamilyName(), userDTO.isTourGuide());
            UserEntity savedUser = userRepository.save(newUser);
            logger.debug("New user created with email: {}", savedUser.getEmail());
            return savedUser; // Return the newly created user
        }
    }

    public boolean updateUserName(String email, String newGivenName, String newFamilyName) {
        Optional<UserEntity> userOptional = userRepository.findById(email);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setFamilyName(newFamilyName);
            user.setGivenName(newGivenName);
            userRepository.save(user);
            return true;
        } else {
            return false; // User not found
        }
    }

    public UserEntity updateUser(String id, UserEntity updatedUser) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        existingUser.setFamilyName(updatedUser.getFamilyName());
        existingUser.setGivenName(updatedUser.getGivenName());
        return userRepository.save(existingUser);
    }


    public boolean deleteUser(String email) {
        logger.debug("deleteUser called with email: {}", email);

        // Check if user exists using case-insensitive email lookup
        Optional<UserEntity> userOptional = userRepository.findByEmailIgnoreCase(email);
        if (userOptional.isPresent()) {
            logger.debug("User found: {}", userOptional.get());

            try {
                // Attempt to delete the user
                userRepository.delete(userOptional.get());
                logger.debug("User with email {} deleted from the database.", email);

                // Notify the authentication service
                notifyAuthenticationService(email);
                logger.debug("Authentication service notified for email: {}", email);

                return true; // Success
            } catch (Exception e) {
                // Log any exceptions during deletion
                logger.error("Error while deleting user with email {}: {}", email, e.getMessage(), e);
                return false; // Indicate failure
            }
        } else {
            logger.warn("No user found with email: {}", email);
        }

        return false; // User not found
    }

    public void notifyAuthenticationService(String email) {
        String authServiceUrl = "http://authentication-service/api/credentials/" + email; // Placeholder until service communication addressed

        try {
            // Send DELETE request to the AuthenticationService
            restTemplate.delete(authServiceUrl);
        } catch (Exception e) {
            // Log the error (optionally handle retries or fallback logic)
            System.err.println("Failed to notify AuthenticationService: " + e.getMessage());
        }
    }

    public boolean becomeTourGuide(String email, String password) {
        // Check if the password is correct
        if ("1234".equals(password)) {
            Optional<UserEntity> userOptional = userRepository.findById(email);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                user.setIsTourGuide(true); // Set isTourGuide to true
                userRepository.save(user);
                return true; // Successfully updated user to be a tour guide
            }
        }
        return false; // Incorrect password or user not found
    }

    public boolean isTourGuide(String email) {
        UserEntity user = userRepository.findById(email)
                .orElseThrow(() -> new UserManagementServiceExceptions("User not found with email: " + email));
        return user.isTourGuide(); // returns the boolean value of isTourGuide
    }
}


