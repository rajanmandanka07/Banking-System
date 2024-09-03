package com.banking.dao;

import com.banking.model.Account;
import com.banking.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    // Method to add a new account to the database
    public void addAccount(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (account_number, user_id, balance, account_type, created_at, password, pin) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, account.getAccountNumber());
            pstmt.setInt(2, account.getUserId());
            pstmt.setDouble(3, account.getBalance());
            pstmt.setString(4, account.getAccountType());
            pstmt.setTimestamp(5, account.getCreatedAt());
            pstmt.setString(6, account.getPassword());
            pstmt.setString(7, account.getPin());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to retrieve an account by account number
    public Account getAccountByNumber(String accountNumber) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt("account_id"),
                        rs.getString("account_number"),
                        rs.getInt("user_id"),
                        rs.getDouble("balance"),
                        rs.getString("account_type"),
                        rs.getTimestamp("created_at"),
                        rs.getString("password"),
                        rs.getString("pin")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // Account not found
    }

    // Method to retrieve an account by its ID
    public Account getAccountById(int accountId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt("account_id"),
                        rs.getString("account_number"),
                        rs.getInt("user_id"),
                        rs.getDouble("balance"),
                        rs.getString("account_type"),
                        rs.getTimestamp("created_at"),
                        rs.getString("password"),
                        rs.getString("pin")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // Account not found
    }

    // Method to update an existing account
    public void updateAccount(Account account) throws SQLException {
        String sql = "UPDATE accounts SET balance = ?, account_type = ?, password = ?, pin = ? WHERE account_number = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, account.getBalance());
            pstmt.setString(2, account.getAccountType());
            pstmt.setString(3, account.getPassword());
            pstmt.setString(4, account.getPin());
            pstmt.setString(5, account.getAccountNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to delete an account by its ID
    public void deleteAccount(int accountId) throws SQLException {
        String sql = "DELETE FROM accounts WHERE account_id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accountId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to retrieve all accounts (for debugging or administrative purposes)
    public List<Account> getAccountsByPhoneNumber(String phoneNumber) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT a.* FROM accounts a " + "JOIN users u ON a.user_id = u.user_id " + "WHERE u.phone_number = ?";

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, phoneNumber);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Account account = new Account(
                            rs.getInt("account_id"),
                            rs.getString("account_number"),
                            rs.getInt("user_id"),
                            rs.getDouble("balance"),
                            rs.getString("account_type"),
                            rs.getTimestamp("created_at"),
                            rs.getString("password"),
                            rs.getString("pin")
                    );
                    accounts.add(account);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving accounts by phone number: " + e.getMessage(), e);
        }
        return accounts;
    }

}
