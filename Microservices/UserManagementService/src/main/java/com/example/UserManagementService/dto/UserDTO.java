package com.example.UserManagementService.dto;

public class UserDTO {
    private String email;
    private String name;
    private boolean isTourGuide;

    public UserDTO(String email, String name, boolean isTourGuide) {
        this.email = email;
        this.name = name;
        this.isTourGuide = isTourGuide;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public boolean isTourGuide() {
        return isTourGuide;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTourGuide(boolean isTourGuide) {
        this.isTourGuide = isTourGuide;
    }
}
