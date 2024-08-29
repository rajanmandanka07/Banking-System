package com.banking.service;

import com.banking.dao.TransactionDAO;
import com.banking.model.Transaction;

import java.util.List;

public class TransactionService {

    private TransactionDAO transactionDAO;

    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
    }

    // Method to record a transaction
    public void recordTransaction(Transaction transaction) {
        try {
            transactionDAO.addTransaction(transaction);
            System.out.println("Transaction recorded successfully!");
        } catch (Exception e) {
            System.err.println("Failed to record transaction: " + e.getMessage());
        }
    }

    // Method to get the transaction history for an account
    public List<Transaction> getAccountTransactionHistory(int accountId) {
        try {
            return transactionDAO.getTransactionsByAccountId(accountId);
        } catch (Exception e) {
            System.err.println("Failed to retrieve transaction history: " + e.getMessage());
            return null;
        }
    }
}
