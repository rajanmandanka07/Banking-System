package com.banking.model;

import java.sql.Timestamp;

public class Transaction {
    // Attributes
    private int transactionId;
    private int accountId;
    private String accountNumber;
    private String transactionType;
    private double amount;
    private Timestamp transactionDate;

    // Constructor for retrieval from DB
    public Transaction(int transactionId, int accountId, String accountNumber, String transactionType, double amount, Timestamp transactionDate) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    // Constructor for new transactions (ID is auto-generated by DB)
    public Transaction(int accountId, String accountNumber, String transactionType, double amount) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = new Timestamp(System.currentTimeMillis());
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

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

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    // toString() method for displaying transaction details
    @Override
    public String toString() {
        return "Transaction {" +
                "Transaction ID = " + transactionId +
                ", Account ID = " + accountId +
                ", Account Number = '" + accountNumber + '\'' +
                ", Transaction Type = '" + transactionType + '\'' +
                ", Amount = " + amount +
                ", Transaction Date = " + transactionDate +
                '}';
    }
}