package com.example.UserManagementService.dto;

public class CreateUserRequest {
    private String givenName;
    private String familyName;
    private String email;
    // Constructor
    public CreateUserRequest(String givenName, String familyName, String email) {
        this.givenName = givenName;
        this.familyName = familyName;
        this.email = email;
    }

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }
    public void setGivenName(String name) {
        this.givenName = name;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
}
