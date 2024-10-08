package com.banking.service;

import com.banking.dao.TransactionDAO;
import com.banking.model.Transaction;

import java.util.List;

public class TransactionService {

    private final TransactionDAO transactionDAO;

    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
    }

    // Method to record a transaction
    public void recordTransaction(Transaction transaction) {
        try {
            transactionDAO.addTransaction(transaction);
        } catch (Exception e) {
            System.err.println("Failed to record transaction: " + e.getMessage());
        }
    }

    // Method to remove transaction
    public void removeTransaction(int accountId) {
        try {
            transactionDAO.deleteTransaction(accountId);
        } catch (Exception e) {
            System.err.println("Failed to record transaction: " + e.getMessage());
        }
    }

    // Method to get the transaction history for an account
    public List<Transaction> getAccountTransactionHistory(String accountNumber) {
        try {
            return transactionDAO.getTransactionsByAccountNumber(accountNumber);
        } catch (Exception e) {
            System.err.println("Failed to retrieve transaction history: " + e.getMessage());
            return null;
        }
    }
}
