package com.example.UserManagementService.controller;

import com.example.UserManagementService.dto.CreateUserRequest;
import com.example.UserManagementService.dto.UpdateUserNameRequest;
import com.example.UserManagementService.dto.UserDTO;
import com.example.UserManagementService.entity.UserEntity;
import com.example.UserManagementService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint to create a new user
    @PostMapping("/createuser")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        UserEntity user = userService.createUser(request.getGivenName(), request.getFamilyName(), request.getEmail());
        return ResponseEntity.ok(user);
    }
    // Endpoint to update the name of an existing user
    @PostMapping("/{email}/update-name")
    public ResponseEntity<?> updateUserName(@PathVariable String email, @RequestParam String givenName, @RequestParam String familyName) {
        boolean updated = userService.updateUserName(email, givenName, familyName);
        if (updated) {
            return ResponseEntity.ok("User name updated successfully");
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam String email) {
        boolean isDeleted = userService.deleteUser(email);
        if (isDeleted) {
            return ResponseEntity.ok("User successfully deleted.");
        } else {
            return ResponseEntity.status(404).body("User not found.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserEntity> handleNewUserAuth(@RequestBody UserDTO userDTO) {
        UserEntity userEntity = userService.handleNewUserLogin(userDTO);
        return ResponseEntity.ok(userEntity);
    }

    @PostMapping("/{email}/become-tour-guide")
    public ResponseEntity<String> becomeTourGuide(@PathVariable String email, @RequestParam String password) {
        boolean success = userService.becomeTourGuide(email, password);
        if (success) {
            return ResponseEntity.ok("User is now a tour guide");
        } else {
            return ResponseEntity.badRequest().body("Failed to become a tour guide: incorrect password or user not found");
        }
    }
    @GetMapping("/{email}/is-tour-guide")
    public ResponseEntity<Boolean> isTourGuide(@PathVariable String email) {
        boolean isTourGuide = userService.isTourGuide(email);
        return ResponseEntity.ok(isTourGuide);
    }

    @GetMapping("/ping")
    public String ping(){
        String ping = "Hello";
        return ping;
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable String id, @RequestBody UserEntity updatedUser) {
        return ResponseEntity.ok(userService.updateUser(id, updatedUser));
    }
}

