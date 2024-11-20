package com.example.AuthenticationService;

import com.example.AuthenticationService.service.AuthenticationService;
import com.example.AuthenticationService.service.JwtService;
import com.example.AuthenticationService.entity.UserEntity;
import com.example.AuthenticationService.repository.UserRepository;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    private UserRepository userRepository;
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        jwtService = mock(JwtService.class);

        authenticationService = new AuthenticationService(
                "test-client-id",
                "test-client-secret",
                "http://localhost:8081/auth/grantcode",
                userRepository,
                jwtService
        );
    }

    @Test
    void testAuthenticationHandler() {
        String email = "user@example.com";
        String jwtToken = "mockJwtToken";

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setRefreshToken("mockJwtToken");
        userEntity.setRefreshTokenExpiry(LocalDate.of(2025, Month.JANUARY, 18).atStartOfDay());
        when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(userEntity));

        when(jwtService.generateToken(email, 3600000)).thenReturn(jwtToken);

        var result = authenticationService.refreshUserToken(email);

        assertEquals(jwtToken, result, "The JWT token should match the mocked one");
        verify(jwtService, times(1)).generateToken(email, 3600000);
    }
//hello
}
