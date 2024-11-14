package com.example.BookingMakingService.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "User")
public class User {

    @Id
    @Column(name = "EmailID")
    private String emailId;

    @Column(nullable = false)
    private String name;

}