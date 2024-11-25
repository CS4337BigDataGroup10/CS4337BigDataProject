package com.example.UserManagementService.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="usermanagementdb")
public class UserEntity {

    @Id
    @Column(unique = true, nullable = false)
    private String email;
    @Column(name="given_name")
    private String givenName;
    @Column(name="family_name")
    private String familyName;
    @Column(name="is_tour_guide")
    private boolean isTourGuide = false; // defaults to false, we can alter in database

    // Default constructor
    public UserEntity() {}

    // Constructor with fields
    public UserEntity(String email, String givenName, String familyName, boolean isTourGuide) {

        this.givenName = givenName;
        this.familyName = familyName;
        this.email = email;
        this.isTourGuide = isTourGuide;
    }


    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isTourGuide() {
        return isTourGuide;
    }



    public void setGivenName(String name) {
        this.givenName = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setIsTourGuide(boolean isTourGuide) {
        this.isTourGuide = isTourGuide;
    }

    public void setFamilyName(String newFamilyName) {
        this.familyName = newFamilyName;
    }


    // Role enum deprecated
    public enum Role {
        USER,           // Represents a customer
        TOUR_GUIDE,     // Represents a tour guide
    }

}
