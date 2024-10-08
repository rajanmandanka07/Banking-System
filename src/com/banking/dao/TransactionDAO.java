package com.banking.dao;

import com.banking.model.Transaction;
import com.banking.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // Method to add a new transaction to the database
    public void addTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (account_id, account_number, transaction_type, amount, transaction_date) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, transaction.getAccountId());
            pstmt.setString(2, transaction.getAccountNumber());
            pstmt.setString(3, transaction.getTransactionType());
            pstmt.setDouble(4, transaction.getAmount());
            pstmt.setTimestamp(5, transaction.getTransactionDate());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to delete transaction on database
    public void deleteTransaction(int accountId) {
        String sql = "DELETE from transactions where account_id = ? ";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accountId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to retrieve all transactions for a specific account by account number
    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE account_number = ?";
        List<Transaction> transactions = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("account_id"),
                        rs.getString("account_number"),
                        rs.getString("transaction_type"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("transaction_date")
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }
}
