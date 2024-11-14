package com.example.UserManagementService.entity;

public class UserEntity {

    private String name;
    private String email;
    private boolean isTourGuide = false; // defaults to false, we can alter in database

    // Default constructor
    public UserEntity() {}

    // Constructor with fields
    public UserEntity( String name, String email, boolean isTourGuide) {

        this.name = name;
        this.email = email;
        this.isTourGuide = isTourGuide;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isTourGuide() {
        return isTourGuide;
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setIsTourGuide(boolean isTourGuide) {
        this.isTourGuide = isTourGuide;
    }


    // Role enum deprecated
    public enum Role {
        USER,           // Represents a customer
        TOUR_GUIDE,     // Represents a tour guide
    }

}
