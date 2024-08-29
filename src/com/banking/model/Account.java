package com.banking.model;

import java.sql.Timestamp;

public class Account {
    // Attributes
    private int accountId;
    private String accountNumber;
    private int userId;
    private double balance;
    private String accountType;
    private Timestamp createdAt;

    // Constructor
    public Account(int accountId, String accountNumber, int userId, double balance, String accountType, Timestamp createdAt) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.balance = balance;
        this.accountType = accountType;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // toString() method for displaying account details
    @Override
    public String toString() {
        return "Account {" +
                "Account ID = " + accountId +
                ", Account Number = '" + accountNumber + '\'' +
                ", User ID = " + userId +
                ", Balance = " + balance +
                ", Account Type = '" + accountType + '\'' +
                ", Created At = " + createdAt +
                '}';
    }
}
