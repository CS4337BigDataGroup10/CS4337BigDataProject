package com.example.UserManagementService.service;

import com.example.UserManagementService.dto.UserDTO;
import com.example.UserManagementService.entity.UserEntity;
import com.example.UserManagementService.repository.UserManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@Service
public class UserService {
    private final UserManagementRepository userRepository;
    private final RestTemplate restTemplate;


    @Autowired
    public UserService(UserManagementRepository userManagementRepository, RestTemplate restTemplate) {
        this.userRepository = userManagementRepository;
        this.restTemplate = restTemplate;
    }


    public UserEntity createUser(String name, String email) {
        UserEntity user = new UserEntity();
        user.setName(name);
        user.setEmail(email);
        user.setIsTourGuide(false); // Default value

        return userRepository.save(user);
    }

    public UserEntity handleUserLogin(UserDTO userDTO) {
        // Check if the user already exists in the database
        return userRepository.findById(userDTO.getEmail())
                .orElseGet(() -> {
                    // If not, create and save a new user
                    UserEntity newUser = new UserEntity(userDTO.getEmail(), userDTO.getName(), userDTO.isTourGuide());
                    return userRepository.save(newUser);
                });
    }

    public boolean updateUserName(String email, String newName) {
        Optional<UserEntity> userOptional = userRepository.findById(email);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setName(newName);
            userRepository.save(user);
            return true;
        } else {
            return false; // User not found
        }
    }


    public boolean deleteUser(String email) {
        Optional<UserEntity> userOptional = userRepository.findById(email);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            userRepository.delete(user);

            notifyAuthenticationService(email);


            return true; //  User was deleted successfully
        }
        return false; // User was not found
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
        Optional<UserEntity> userOptional = userRepository.findById(email);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            return user.isTourGuide(); // returns the boolean value of isTourGuide
        }
        return false; // user is not found 
    }
}


