package com.banking.controller;

import com.banking.exception.InsufficientFundsException;
import com.banking.model.Account;
import com.banking.model.User;
import com.banking.service.AccountService;
import com.banking.service.TransactionService;
import com.banking.service.UserService;
import com.banking.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingController {

    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final Scanner scanner;
    private final Connection connection;

    public BankingController() {
        this.userService = new UserService();
        this.accountService = new AccountService();
        this.transactionService = new TransactionService();
        this.scanner = new Scanner(System.in);
        this.connection = DatabaseConnection.getConnection();
    }

    // Handle user registration
    public void handleUserRegistration() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your phone number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter your address: ");
        String address = scanner.nextLine();

        // Create User object
        User user = new User(name, email, phoneNumber, address);

        // Register the user
        boolean isRegistered = userService.registerUser(user);

        // Check if registration was successful
        if (isRegistered) {
            System.out.println("User registered successfully.");
            System.out.println("User Details:");
            System.out.println(user);
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }

    // Handle account creation
    public void handleAccountCreation() {
        System.out.print("Enter user phone number for account creation: ");
        String phoneNumber = scanner.nextLine();

        // Fetch the user ID associated with the phone number
        Integer userId = userService.getUserIdByPhoneNumber(phoneNumber);
        if (userId == null) {
            System.out.println("User not found with the provided phone number. Please register first.");
            return;
        }
        System.out.println("User ID: " + userId);

        System.out.print("Enter account type (SAVINGS/CURRENT): ");
        String accountType = scanner.nextLine().toUpperCase();
        System.out.print("Enter account password: ");
        String password = scanner.nextLine();
        System.out.print("Enter PIN (4 digits): ");
        String pin = scanner.nextLine();

        // Validate accountType
        if (!accountType.equals("SAVINGS") && !accountType.equals("CURRENT")) {
            System.out.println("Invalid account type. Please enter 'SAVINGS' or 'CURRENT'.");
            return;
        }

        // Generate a new account number
        String accountNumber = generateAccountNumber();

        // Create Account object
        Account account = new Account(userId, accountNumber, userId, 0.0, accountType, new java.sql.Timestamp(System.currentTimeMillis()), password, pin);

        // Create account using accountService
        boolean isAccountCreated = accountService.createAccount(account);

        // Check if account creation was successful
        if (isAccountCreated) {
            System.out.println("Account created successfully.");
            System.out.println("Account Details:");
            System.out.println(account);
        } else {
            System.out.println("Account creation failed. Please try again.");
        }
    }

    // Handle user login
    public boolean handleUserLogin() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter account password: ");
        String password = scanner.nextLine();

        // Validate user login
        Account account = accountService.authenticateAccountByPassword(accountNumber, password);
        if (account != null) {
            System.out.println("Login successful.");
            return true;
        } else {
            System.out.println("Invalid account number or password.");
            return false;
        }
    }

    // Handle deposit
    public void handleDeposit() {
        System.out.print("Enter account number for deposit: ");
        String accountNumber = scanner.nextLine();

        System.out.print("Enter deposit amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        // Authenticate the account using the account number and PIN
        Account account = accountService.authenticateAccountByPin(accountNumber, pin);
        if (account != null) {
            accountService.deposit(account.getAccountId(), amount);
//            System.out.println("Deposit successful.");
        } else {
            System.out.println("Invalid account number or PIN. Deposit failed.");
        }
    }

    /// Handle withdrawal
    public void handleWithdrawal() {
        try {
            System.out.print("Enter account number for withdrawal: ");
            String accountNumber = scanner.nextLine();

            System.out.print("Enter withdrawal amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();  // Consume the leftover newline

            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine();

            // Authenticate the account using the account number and PIN
            Account account = accountService.authenticateAccountByPin(accountNumber, pin);
            if (account != null) {
                accountService.withdraw(account.getAccountId(), amount);
//                System.out.println("Withdrawal successful.");
            } else {
                System.out.println("Invalid account number or PIN. Withdrawal failed.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Handle fund transfer
    public void handleFundTransfer() {
        try {
            System.out.print("Enter source account number: ");
            String fromAccountNumber = scanner.nextLine();

            System.out.print("Enter destination account number: ");
            String toAccountNumber = scanner.nextLine();

            System.out.print("Enter transfer amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();

            System.out.print("Enter PIN for source account: ");
            String sourcePin = scanner.nextLine();

            // Perform fund transfer
            accountService.transferFunds(fromAccountNumber, toAccountNumber, amount, sourcePin);
            System.out.println("Fund transfer successful.");
        } catch (InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Handle viewing details
    public void handleViewDetails() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        Account account = accountService.getAccountByAccountNumber(accountNumber);

        if (account != null) {
            System.out.println("Account Details:");
            System.out.println(account);
        } else {
            System.out.println("Invalid account number.");
        }
    }

    // Handle viewing transactions
    public void handleViewTransactions() {
        System.out.print("Enter account number to view transactions: ");
        String accountNumber = scanner.nextLine();

        transactionService.getAccountTransactionHistory(accountNumber).forEach(System.out::println);
    }

    // Method to exit the application and close the database connection
    public void exitApplication() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close database connection: " + e.getMessage());
        }
    }

    // Method to Generate a 12-digit account number
    private String generateAccountNumber() {
        long number = (long) (Math.random() * 1_000_000_000_000L);
        return String.format("%012d", number);
    }
}
