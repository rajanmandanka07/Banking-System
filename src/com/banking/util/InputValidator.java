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
        return user != null;
//        return validateUsername(user.getName()) &&
//                validateEmail(user.getEmail()) &&
//                validatePhoneNumber(user.getPhoneNumber()) &&
//                user.getUserId() > 0;
    }

    // Validate account details
    public static boolean isValidAccount(Account account) {
        if (account == null) {
            return false;
        }
        return validateAccountNumber(account.getAccountNumber()) &&
                account.getUserId() > 0 &&
                account.getBalance() >= 0 &&
                account.getAccountType() != null && !account.getAccountType().isEmpty();
    }
}
