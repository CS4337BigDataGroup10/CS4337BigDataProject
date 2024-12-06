package com.example.BookingMakingService.service;


import com.example.BookingMakingService.exceptions.TokenExpiredException;
import com.example.BookingMakingService.exceptions.TokenInvalidException;
import com.example.BookingMakingService.exceptions.TokenMissingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    private final SecretKey JwtSecret;

    public JwtService(@Value("${jwt.secret}") String googleClientSecret) {
        this.JwtSecret = Keys.hmacShaKeyFor(googleClientSecret.getBytes());
    }

    public Claims validateToken(String token) throws Exception {
        try {
            return Jwts.parser()
                    .setSigningKey(JwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            if (e.getMessage().contains("expired")) {
                throw new TokenExpiredException();
            } else {
                throw new TokenInvalidException();
            }
        } catch (IllegalArgumentException e) {
            throw new TokenMissingException();
        }
    }
}

