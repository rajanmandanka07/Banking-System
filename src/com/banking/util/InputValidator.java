package com.banking.util;

import com.banking.model.Account;
import com.banking.model.User;

import java.util.regex.Pattern;

public class InputValidator {

    // Regular expression for validating email
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    // Regular expression for validating phone number (assuming 10-digit numbers)
    private static final String PHONE_REGEX = "^[0-9]{10}$";

    // Regular expression for validating account number (assuming 12-digit numbers)
    private static final String ACCOUNT_NUMBER_REGEX = "^[0-9]{12}$";

    // Regular expression for validating username
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_]{3,15}$";

    // Validate email address
    public static boolean validateEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    // Validate phone number
    public static boolean validatePhoneNumber(String phoneNumber) {
        return Pattern.matches(PHONE_REGEX, phoneNumber);
    }

    // Validate account number
    public static boolean validateAccountNumber(String accountNumber) {
        return Pattern.matches(ACCOUNT_NUMBER_REGEX, accountNumber);
    }

    // Validate username
    public static boolean validateUsername(String username) {
        return Pattern.matches(USERNAME_REGEX, username);
    }

    // Validate user details
    public static boolean isValidUser(User user) {
        if (user == null) {
            System.out.println("User object is null.");
            return false;
        }

        if (user.getName() == null) {
            System.out.println("Username is null.");
            return false;
        }

        if (user.getEmail() == null) {
            System.out.println("Email is null.");
            return false;
        }

        if (user.getPhoneNumber() == null) {
            System.out.println("Phone number is null.");
            return false;
        }

        boolean isUsernameValid = validateUsername(user.getName());
        boolean isEmailValid = validateEmail(user.getEmail());
        boolean isPhoneNumberValid = validatePhoneNumber(user.getPhoneNumber());

        if (!isUsernameValid) {
            System.out.println("Username validation failed: " + user.getName());
        }

        if (!isEmailValid) {
            System.out.println("Email validation failed: " + user.getEmail());
        }

        if (!isPhoneNumberValid) {
            System.out.println("Phone number validation failed: " + user.getPhoneNumber());
        }

        return isUsernameValid && isEmailValid && isPhoneNumberValid;
    }

    // Validate account details
    public static boolean isValidAccount(Account account) {
        if (account == null) {
            System.out.println("Account object is null.");
            return false;
        }

        // Validate account number
        boolean isAccountNumberValid = validateAccountNumber(account.getAccountNumber());
        if (!isAccountNumberValid) {
            System.out.println("Account number validation failed: " + account.getAccountNumber());
        }

        // Validate user ID
        boolean isUserIdValid = account.getUserId() > 0;
        if (!isUserIdValid) {
            System.out.println("User ID validation failed: " + account.getUserId());
        }

        // Validate balance
        boolean isBalanceValid = account.getBalance() >= 0;
        if (!isBalanceValid) {
            System.out.println("Balance validation failed: " + account.getBalance());
        }

        // Validate account type
        boolean isAccountTypeValid = account.getAccountType() != null && !account.getAccountType().isEmpty();
        if (!isAccountTypeValid) {
            System.out.println("Account type validation failed: " + account.getAccountType());
        }

        // Return the combined result of all validations
        return isAccountNumberValid && isUserIdValid && isBalanceValid && isAccountTypeValid;
    }

}
