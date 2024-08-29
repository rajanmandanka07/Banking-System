package com.banking.model;

public class User {
    // Attributes
    private int userId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;

    // Constructor
    public User(String name, String email, String phoneNumber, String address) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // toString() method for displaying user details
    @Override
    public String toString() {
        return "User {" +
                "User ID = " + userId +
                ", Name = '" + name + '\'' +
                ", Email = '" + email + '\'' +
                ", Phone Number = '" + phoneNumber + '\'' +
                ", Address = '" + address + '\'' +
                '}';
    }
}
