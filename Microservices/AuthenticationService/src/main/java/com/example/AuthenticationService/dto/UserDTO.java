package com.example.AuthenticationService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDTO {
    @JsonProperty("email")
    private String email;
    @JsonProperty("givenName")
    private String givenName;
    @JsonProperty("familyName")
    private String familyName;
    @JsonProperty("isTourGuide")
    private boolean isTourGuide;

    public UserDTO() {}

    public UserDTO(String email, String givenName, String familyName,  Boolean isTourGuide) {
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



    public void setTourGuide(boolean tourGuide) {
        isTourGuide = tourGuide;
    }

    public boolean isTourGuide() {
        return isTourGuide;
    }


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