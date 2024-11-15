package com.example.UserManagementService.controller;

import com.example.UserManagementService.dto.UserDTO;
import com.example.UserManagementService.entity.UserEntity;
import com.example.UserManagementService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserEntity> handleUserAuth(@RequestBody UserDTO userDTO) {
        UserEntity userEntity = userService.handleUserLogin(userDTO);
        return ResponseEntity.ok(userEntity);
    }
}
