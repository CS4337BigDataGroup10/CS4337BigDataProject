package com.example.TourManagementService.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "User")
public class User {

    @Id
    @Column(name = "EmailID", nullable = false, length = 255)
    private String emailId;

    @Column(name = "Name", nullable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "UserType", nullable = false, length = 20)
    private UserType userType;

    // Getters and Setters

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public enum UserType {
        USER("user"),
        TOUR_GUIDE("tour guide");

        private final String value;

        UserType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
