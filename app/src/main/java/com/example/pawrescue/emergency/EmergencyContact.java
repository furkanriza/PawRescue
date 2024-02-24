package com.example.pawrescue.emergency;

public class EmergencyContact {
    private String name;
    private String phoneNumber;
    private String description;
    private String hours;

    public EmergencyContact(String name, String phoneNumber, String description, String hours) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.hours = hours;
    }

    // Getters
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getDescription() { return description; }
    public String getHours() { return hours; }

    // You might want a toString method for easy display in the ListView
    @Override
    public String toString() {
        return "Name: " + name + "\nPhone: " + phoneNumber + "\nDescription: " + description + "\nHours: " + hours;
    }
}
