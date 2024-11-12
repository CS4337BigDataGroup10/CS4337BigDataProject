package com.example.BookingMakingService.entity;


import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "User")
public class User {

    @Id
    @Column(name = "EmailID")
    private String emailId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    // Getters and Setters

    public enum UserType {
        USER, TOUR_GUIDE, ADMIN
    }
}