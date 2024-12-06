package com.example.AuthenticationService.service;

import com.example.AuthenticationService.exceptions.JwtServiceExceptions;
import io.jsonwebtoken.*;
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

    public String extractSubject(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(JwtSecret) // Set the secret key to verify the signature
                    .parseClaimsJws(jwt)
                    .getBody();

            return claims.getSubject();
        } catch (SignatureException e) {
            throw new RuntimeException("Invalid JWT signature", e);
        } catch (Exception e) {
            throw new RuntimeException("Error while parsing the JWT", e);
        }
    }

    public String generateToken(String email) {
        long oneHourInMillis = 60 * 60 * 1000;

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
