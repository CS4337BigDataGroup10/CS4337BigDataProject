package com.example.UserManagementService.controller;

import com.example.UserManagementService.dto.CreateUserRequest;
import com.example.UserManagementService.dto.UserDTO;
import com.example.UserManagementService.entity.UserEntity;
import com.example.UserManagementService.repository.UserManagementRepository;
import com.example.UserManagementService.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "APIs for managing user accounts")
public class UserController {

    private final UserService userService;
    private final UserManagementRepository userManagementRepository;

    @Autowired
    public UserController(UserService userService, UserManagementRepository userManagementRepository) {
        this.userService = userService;
        this.userManagementRepository = userManagementRepository;
    }

    @Operation(
            summary = "Create a new user",
            description = "Creates a new user with the provided details",
            requestBody = @RequestBody(description = "Details of the user to be created", content = @Content(schema = @Schema(implementation = CreateUserRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserEntity.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid user data provided")
            }
    )
    @PostMapping("/createuser")
    public String createUser(@RequestBody RequestBody userDto) {
        System.out.println(userDto.toString());
       return userDto.toString();
    }





    @Operation(
            summary = "Update user name",
            description = "Updates the first and last name of a user by email",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User name updated successfully"),
                    @ApiResponse(responseCode = "400", description = "User not found")
            }
    )
    @PostMapping("/{email}/update-name")
    public ResponseEntity<?> updateUserName(@PathVariable String email, @RequestParam String givenName, @RequestParam String familyName) {
        boolean updated = userService.updateUserName(email, givenName, familyName);
        if (updated) {
            return ResponseEntity.ok("User name updated successfully");
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

    @Operation(
            summary = "Delete a user",
            description = "Deletes a user by email",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam String email) {
        boolean isDeleted = userService.deleteUser(email);
        if (isDeleted) {
            return ResponseEntity.ok("User successfully deleted.");
        } else {
            return ResponseEntity.status(404).body("User not found.");
        }
    }

    @Operation(
            summary = "Handle new user authentication",
            description = "Processes a new user login and returns user details",
            requestBody = @RequestBody(description = "User login details", content = @Content(schema = @Schema(implementation = UserDTO.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User authenticated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserEntity.class)))
            }
    )
    @PostMapping("/login")
    public ResponseEntity<UserEntity> handleNewUserAuth(@RequestBody UserDTO userDTO) {
        UserEntity userEntity = userService.handleNewUserLogin(userDTO);
        return ResponseEntity.ok(userEntity);
    }

    @Operation(
            summary = "Become a tour guide",
            description = "Allows a user to become a tour guide if they provide a correct password",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User is now a tour guide"),
                    @ApiResponse(responseCode = "400", description = "Failed to become a tour guide")
            }
    )
    @PostMapping("/{email}/become-tour-guide")
    public ResponseEntity<String> becomeTourGuide(@PathVariable String email, @RequestParam String password) {
        boolean success = userService.becomeTourGuide(email, password);
        if (success) {
            return ResponseEntity.ok("User is now a tour guide");
        } else {
            return ResponseEntity.badRequest().body("Failed to become a tour guide: incorrect password or user not found");
        }
    }

    @Operation(
            summary = "Check if user is a tour guide",
            description = "Checks whether a user is a tour guide",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved tour guide status",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/{email}/is-tour-guide")
    public ResponseEntity<Boolean> isTourGuide(@PathVariable String email) {
        boolean isTourGuide = userService.isTourGuide(email);
        return ResponseEntity.ok(isTourGuide);
    }

}
