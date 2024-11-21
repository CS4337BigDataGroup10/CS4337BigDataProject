package com.example.AuthenticationService;

import com.example.AuthenticationService.exceptions.JwtServiceExceptions;
import com.example.AuthenticationService.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JwtServiceTest {

    private JwtService jwtService;
    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        String secret = "my-very-secure-and-long-secret-key";
        secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        jwtService = new JwtService(secret);
    }

    @Test
    void testValidateToken_ValidToken() {
        // Arrange: Create a valid token
        String email = "test@example.com";
        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        Claims claims = jwtService.validateToken(token);

        assertNotNull(claims);
        assertNotNull(claims.getSubject());
        assert(claims.getSubject().equals(email));
    }

    @Test
    void testValidateToken_ExpiredToken() {
        String email = "test@example.com";
        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis() - 2 * 60 * 60 * 1000))
                .setExpiration(new Date(System.currentTimeMillis() - 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        assertThrows(JwtServiceExceptions.TokenExpiredException.class, () -> jwtService.validateToken(token));
    }

    @Test
    void testValidateToken_InvalidToken() {
        // Arrange: Create a malformed/invalid token
        String token = "invalid.token.string";

        assertThrows(JwtServiceExceptions.InvalidTokenException.class, () -> jwtService.validateToken(token));
    }
}
