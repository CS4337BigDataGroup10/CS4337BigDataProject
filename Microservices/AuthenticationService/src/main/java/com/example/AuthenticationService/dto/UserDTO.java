package com.example.AuthenticationService.dto;

public class UserDTO {
    private String email;
    private String givenName; // First name
    private String familyName; // Last name
    private String profilePicture; // Profile picture URL
    private String jwtToken; // JWT token
    private String role; // User's role (e.g., ADMIN, USER, etc.)

    // Default constructor
    public UserDTO() {}

    public UserDTO(String email, String givenName, String familyName, String profilePicture, String jwtToken, String role) {
        this.email = email;
        this.givenName = givenName;
        this.familyName = familyName;
        this.profilePicture = profilePicture;
        this.jwtToken = jwtToken;
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

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
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
                ", jwtToken='" + jwtToken + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
