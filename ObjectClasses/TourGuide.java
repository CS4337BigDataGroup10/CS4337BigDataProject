package com.example.BigDataProject;

import java.util.ArrayList;
import java.util.Date;

public class TourGuide {

    // Private variables
    private String name;
    private String email;
    private String phoneNumber;
    private ArrayList<Date> holidays;  // List to store available workdays

    // Constructor
    public TourGuide(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.holidays = new ArrayList<>(); // Initialize an empty list for available days
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<Date> getHolidays() {
        return holidays;
    }

    public void setHolidays(ArrayList<Date> holidays) {
        this.holidays = holidays;
    }

    public void requestHoliday(Date date) {
        // request holiday
    }

    public void removeHoliday(String day) {
        // remove holidays
    }

    // Method to display available days
    public void displayHolidays() {
        System.out.println("Holidays: ");
        for (Date holiday : holidays){
            System.out.println(holiday + ", ");
        }
    }

}
