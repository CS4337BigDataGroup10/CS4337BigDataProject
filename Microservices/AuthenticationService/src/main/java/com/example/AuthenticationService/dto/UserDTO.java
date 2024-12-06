package com.example.AuthenticationService.dto;

public class UserDTO {
    private String email;
    private String givenName;
    private String familyName;
    private boolean isTourGuide;  // Corresponds to the 'is_tour_guide' column in the schema

    // Default constructor
    public UserDTO() {}

    public UserDTO(String email, String givenName, String familyName, boolean isTourGuide) {
        this.email = email;
        this.givenName = givenName;
        this.familyName = familyName;
        this.isTourGuide = isTourGuide;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public boolean isTourGuide() {
        return isTourGuide;
    }

    public void setTourGuide(boolean isTourGuide) {
        this.isTourGuide = isTourGuide;
    }

    // Optional: toString method
    @Override
    public String toString() {
        return "UserDTO{" +
                "email='" + email + '\'' +
                ", givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", isTourGuide=" + isTourGuide +
                '}';
    }
}
