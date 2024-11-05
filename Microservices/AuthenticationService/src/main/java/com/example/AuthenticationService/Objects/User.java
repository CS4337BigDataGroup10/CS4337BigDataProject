package com.example.AuthenticationService.Objects;

public class User {
    String id;
    String name;
    String given_name;
    String family_name;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setVerified_email(boolean verified_email) {
        this.verified_email = verified_email;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    String email;
    boolean verified_email;

    public boolean isVerified_email() {
        return verified_email;
    }

    public String getPicture() {
        return picture;
    }

    public String getEmail() {
        return email;
    }

    public String getFamily_name() {
        return family_name;
    }

    public String getGiven_name() {
        return given_name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    String picture;
}


