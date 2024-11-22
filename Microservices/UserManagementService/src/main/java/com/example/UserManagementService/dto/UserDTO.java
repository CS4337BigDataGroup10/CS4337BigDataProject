package com.example.UserManagementService.dto;

public class UserDTO {
    private String email;
    private String givenName;
    private String familyName;
    private String profilePicture;
    private boolean isTourGuide;

    // Default constructor
    public UserDTO() {}

    public UserDTO(String email, String givenName, String familyName, String profilePicture, Boolean isTourGuide) {
        this.email = email;
        this.givenName = givenName;
        this.familyName = familyName;
        this.profilePicture = profilePicture;
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setTourGuide(boolean tourGuide) {
        isTourGuide = tourGuide;
    }

    public boolean isTourGuide() {
        return isTourGuide;
    }


    // Optional: toString method
    @Override
    public String toString() {
        return "UserDTO{" +
                "email='" + email +
        ", givenName='" + givenName +
        ", familyName='" + familyName +
        ", profilePicture='" + profilePicture +
        ", isTourGuide='" + isTourGuide() +
        '}';
    }
}

