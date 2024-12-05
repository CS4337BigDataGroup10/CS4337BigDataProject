package com.example.AuthenticationService.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "AuthenticatedUsers")
public class UserEntity {
    @Id
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;

    @Column(name = "refresh_token_expiry")
    private LocalDateTime refreshTokenExpiry;


    public UserEntity() {}

    public UserEntity(String email, String refreshToken, LocalDateTime refreshTokenExpiry) {
        this.email = email;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiry = refreshTokenExpiry;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public LocalDateTime getRefreshTokenExpiry() {
        return refreshTokenExpiry;
    }

    public void setRefreshTokenExpiry(LocalDateTime refreshTokenExpiry) {
        this.refreshTokenExpiry = refreshTokenExpiry;
    }
}
