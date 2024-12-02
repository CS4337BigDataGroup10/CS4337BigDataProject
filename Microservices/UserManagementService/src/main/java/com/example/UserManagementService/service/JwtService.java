package com.example.UserManagementService.service;

import com.example.UserManagementService.exceptions.JwtServiceExceptions;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey JwtSecret;

    public JwtService(@Value("${Jwt.Secret}") String JwtSecret) {
        this.JwtSecret = Keys.hmacShaKeyFor(JwtSecret.getBytes());
    }

    public String generateToken(String email) {
        // Define the token expiration time (1 hour in milliseconds)
        long oneHourInMillis = 60 * 60 * 1000;

        // Generate the token
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + oneHourInMillis))
                .signWith(SignatureAlgorithm.HS256, JwtSecret)
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(JwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtServiceExceptions.TokenExpiredException("Token has expired", e);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtServiceExceptions.InvalidTokenException("Token is invalid", e);
        }
    }
}
