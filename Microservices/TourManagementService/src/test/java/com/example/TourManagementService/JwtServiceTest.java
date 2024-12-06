package com.example.TourManagementService;

import com.example.TourManagementService.exceptions.JwtServiceExceptions;
import com.example.TourManagementService.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private final String secretKey = "SecretKey1234567898765432123456789";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(secretKey);
    }

    @Test
    void testGenerateToken_Success() {
        String email = "test@example.com";

        String token = jwtService.generateToken(email);

        assertNotNull(token);
        Claims claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .parseClaimsJws(token)
                .getBody();
        assertEquals(email, claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    void testValidateToken_ValidToken() {
        String email = "test@example.com";
        String token = jwtService.generateToken(email);

        Claims claims = jwtService.validateToken(token);

        assertNotNull(claims);
        assertEquals(email, claims.getSubject());
    }

    @Test
    void testValidateToken_ExpiredToken() {
        // Creates a token with a short expiration time
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        String expiredToken = Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 2 * 60 * 60 * 1000)) // 2 hours ago
                .setExpiration(new Date(System.currentTimeMillis() - 1 * 60 * 60 * 1000)) // 1 hour ago
                .signWith(key)
                .compact();

        JwtServiceExceptions.TokenExpiredException exception = assertThrows(
                JwtServiceExceptions.TokenExpiredException.class,
                () -> jwtService.validateToken(expiredToken)
        );

        assertEquals("Token has expired", exception.getMessage());
    }

    @Test
    void testValidateToken_InvalidToken() {
        String invalidToken = "invalid.token.string";

        JwtServiceExceptions.InvalidTokenException exception = assertThrows(
                JwtServiceExceptions.InvalidTokenException.class,
                () -> jwtService.validateToken(invalidToken)
        );

        assertEquals("Token is invalid", exception.getMessage());
    }

    @Test
    void testValidateToken_IllegalArgument() {
        JwtServiceExceptions.InvalidTokenException exception = assertThrows(
                JwtServiceExceptions.InvalidTokenException.class,
                () -> jwtService.validateToken(null)
        );

        assertEquals("Token is invalid", exception.getMessage());
    }
}
