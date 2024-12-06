package com.example.AuthenticationService.service;

import com.example.AuthenticationService.exceptions.TokenExpiredException;
import com.example.AuthenticationService.exceptions.TokenInvalidException;
import com.example.AuthenticationService.exceptions.TokenMissingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey JwtSecret;

    public JwtService(@Value("${jwt.secret}") String googleClientSecret) {
        this.JwtSecret = Keys.hmacShaKeyFor(googleClientSecret.getBytes());
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
