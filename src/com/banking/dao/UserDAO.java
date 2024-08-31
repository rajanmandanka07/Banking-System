package com.banking.dao;

import com.banking.model.User;
import com.banking.util.DatabaseConnection;

import java.sql.*;

public class UserDAO {

    // Method to add a user to the database
    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (name, email, phone_number, address) VALUES (?, ?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhoneNumber());
            pstmt.setString(4, user.getAddress());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to get a user by their ID
    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("address")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // User not found
    }

    // Method to update an existing user
    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET name = ?, email = ?, phone_number = ?, address = ? WHERE user_id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhoneNumber());
            pstmt.setString(4, user.getAddress());
            pstmt.setInt(5, user.getUserId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to delete a user by their ID
    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to get a user by phone number
    public User getUserByPhoneNumber(String phoneNumber) throws SQLException {
        String sql = "SELECT * FROM users WHERE phone_number = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phoneNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Assuming you have a User constructor or setter methods to populate the User object
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            phoneNumber,  // Phone number from ResultSet
                            rs.getString("address")
                    );
                } else {
                    return null; // No user found with the provided phone number
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
