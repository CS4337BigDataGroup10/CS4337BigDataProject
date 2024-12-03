package com.example.TourManagementService.service;

import com.example.AuthenticationService.entity.UserEntity;
import com.example.AuthenticationService.exceptions.AuthenticationServiceExceptions;
import com.example.TourManagementService.exceptions.JwtServiceExceptions;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey JwtSecret;

    public JwtService(@Value("${Jwt.Secret}") String JwtSecret) {
        this.JwtSecret = Keys.hmacShaKeyFor(JwtSecret.getBytes());
    }
    public Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(JwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtServiceExceptions.TokenExpiredException("Token has expired, please sign in again", e);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtServiceExceptions.InvalidTokenException("Token is invalid", e);
        }
    }

}

