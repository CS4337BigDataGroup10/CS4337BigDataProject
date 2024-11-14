package com.example.AuthenticationService.dto;

public class UserDTO {
    private String email;
    private String profilePicture;
    private String jwtToken;  // Assuming you want to include the JWT token in the DTO

    // Default constructor
    public UserDTO() {}

    // Constructor with fields
    public UserDTO(String email, String profilePicture, String jwtToken) {
        this.email = email;
        this.profilePicture = profilePicture;
        this.jwtToken = jwtToken;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    // Optional: toString method
    @Override
    public String toString() {
        return "UserDTO{" +
                "email='" + email + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", jwtToken='" + jwtToken + '\'' +
                '}';
    }
}
