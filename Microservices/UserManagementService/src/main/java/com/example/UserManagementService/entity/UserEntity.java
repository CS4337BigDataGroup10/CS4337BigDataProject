package com.example.UserManagementService.entity;
import java.time.LocalDateTime;

public class UserEntity {
    private Long id;
    private String name;
    private String email;
    private Role role;

    // Default constructor
    public UserEntity() {}

    // Constructor with fields
    public UserEntity(Long id, String name, String email, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setRole(Role role) {
        this.role = role;
    }


    // Role enum
    public enum Role {
        USER,           // Represents a customer
        TOUR_GUIDE,     // Represents a tour guide
        ADMIN           // Represents an admin
    }

}
