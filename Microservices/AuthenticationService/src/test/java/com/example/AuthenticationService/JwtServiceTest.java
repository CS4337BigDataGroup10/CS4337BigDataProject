package com.example.AuthenticationService;

import com.example.AuthenticationService.service.JwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        String secret = "test-google-client-secret-test-google-client-secret"; // Ensure it's 256-bit
        jwtService = new JwtService(secret);
    }

    @Test
    void testGenerateAndValidateToken() {
        String email = "test@example.com";
        long expirationTime = 3600000; // 1 hour

        String token = jwtService.generateToken(email, expirationTime);
        assertNotNull(token, "Generated token should not be null");

        Claims claims = jwtService.validateToken(token);
        assertNotNull(claims, "Token claims should not be null");
        assertEquals(email, claims.getSubject(), "Email should match the subject in the token");
    }

    @Test
    void testExpiredTokenThrowsException() {
        String email = "test@example.com";
        long expirationTime = -1000; // Expired

        String token = jwtService.generateToken(email, expirationTime);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            jwtService.validateToken(token);
        });

        assertTrue(exception.getMessage().contains("Token has expired"));
    }
}
