package com.banking.service;

import com.banking.dao.UserDAO;
import com.banking.exception.UserNotFoundException;
import com.banking.model.User;
import com.banking.util.InputValidator;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    // Method to register a new user
    public boolean registerUser(User user) {
        if (InputValidator.isValidUser(user)) {
            try {
                userDAO.addUser(user);
            } catch (Exception e) {
                System.err.println("Failed to register user: " + e.getMessage());
            }
        } else {
            System.err.println("Invalid user data provided.");
        }
        return true;
    }

    // Method to update user details
    public void updateUserDetails(int userId, User user) {
        try {
            User existingUser = userDAO.getUserById(userId);
            if (existingUser != null) {
                if (InputValidator.isValidUser(user)) {
                    user.setUserId(userId);
                    userDAO.updateUser(user);
                    System.out.println("User details updated successfully!");
                } else {
                    System.err.println("Invalid user data provided.");
                }
            } else {
                throw new UserNotFoundException("User not found with ID: " + userId);
            }
        } catch (UserNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to update user details: " + e.getMessage());
        }
    }

    // Method to delete a user by ID
    public void deleteUser(int userId) {
        try {
            User existingUser = userDAO.getUserById(userId);
            if (existingUser != null) {
                userDAO.deleteUser(userId);
                System.out.println("User deleted successfully!");
            } else {
                throw new UserNotFoundException("User not found with ID: " + userId);
            }
        } catch (UserNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to delete user: " + e.getMessage());
        }
    }

    // Method to get user ID by phone number
    public Integer getUserIdByPhoneNumber(String phoneNumber) {
        try {
            // Fetch user details using the phone number
            User user = userDAO.getUserByPhoneNumber(phoneNumber);

            if (user != null) {
                return user.getUserId();
            } else {
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error fetching user by phone number: " + e.getMessage());
            return null;
        }
    }
}
