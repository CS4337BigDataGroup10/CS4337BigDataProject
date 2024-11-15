package com.example.AuthenticationService.controller;

import com.example.AuthenticationService.dto.UserDTO;
import com.example.AuthenticationService.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationServiceController {

    private final AuthenticationService authenticationService;

    public AuthenticationServiceController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/grantcode")
    public ResponseEntity<?> grantCode(@RequestParam(value = "code", required = false) String code,
                                       @RequestParam(value = "scope", required = false) String scope,
                                       @RequestParam(value = "authuser", required = false) String authUser,
                                       @RequestParam(value = "prompt", required = false) String prompt) {
        try {
            System.out.println("Code: " + code);
            System.out.println("Scope: " + scope);
            System.out.println("AuthUser: " + authUser);
            System.out.println("Prompt: " + prompt);

            if (code == null) {
                return ResponseEntity.badRequest().body("Authorization code is missing");
            }

            UserDTO user = authenticationService.authenticationHandler(code);
            return ResponseEntity.ok(user); // Return user as JSON
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}

