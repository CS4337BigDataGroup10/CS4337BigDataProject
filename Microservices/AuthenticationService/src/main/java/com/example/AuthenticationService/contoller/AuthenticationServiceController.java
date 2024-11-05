package com.example.AuthenticationService.contoller;

import com.example.AuthenticationService.Objects.User;
import com.example.AuthenticationService.dto.UserDTO;
import com.example.AuthenticationService.repository.AuthenticationServiceRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.AuthenticationService.service.AuthenticationService;

@RestController
public class AuthenticationServiceController {
    private final AuthenticationService authenticationService;

    public AuthenticationServiceController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService; // Use 'service' consistently
    }

    @GetMapping("/grantcode")
    public String grantCode(@RequestParam("code") String code,
                            @RequestParam("scope") String scope,
                            @RequestParam("authuser") String authUser,
                            @RequestParam("prompt") String prompt) {
        User user = authenticationService.returnUserInfo(code);
        return user.toString();
    }
}
