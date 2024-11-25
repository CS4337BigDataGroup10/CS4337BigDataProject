package com.example.AuthenticationService.dto;

public class UserDTO {
    private String email;
    private String givenName;
    private String familyName;
    private String profilePicture;
    private String role;

    // Default constructor
    public UserDTO() {}

    public UserDTO(String email, String givenName, String familyName, String profilePicture, String role) {
        this.email = email;
        this.givenName = givenName;
        this.familyName = familyName;
        this.profilePicture = profilePicture;
        this.role = role;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Optional: toString method
    @Override
    public String toString() {
        return "UserDTO{" +
                "email='" + email + '\'' +
                ", givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
