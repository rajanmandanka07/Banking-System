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
        System.out.println(userId);
        if (userId == null) {
            System.out.println("User not found with the provided phone number. Please register first.");
            return;
        }

        System.out.println("Enter account type (SAVINGS/CURRENT):");
        String accountType = scanner.nextLine().toUpperCase();
        System.out.println("Enter account password:");
        String password = scanner.nextLine();
        System.out.println("Enter PIN (4 digits):");
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
        System.out.print("Enter PIN: ");
        String pin = scanner.next();

        // Authenticate the account using the account number and PIN
        Account account = accountService.authenticateAccountByPin(accountNumber, pin);
        System.out.println(account);
        if (account != null) {
            accountService.deposit(account.getAccountId(), amount);
            System.out.println("Deposit successful.");
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
            System.out.print("Enter PIN: ");
            String pin = scanner.next();

            // Authenticate the account using the account number and PIN
            Account account = accountService.authenticateAccountByPin(accountNumber, pin);
            System.out.println(account);
            if (account != null) {
                accountService.withdraw(account.getAccountId(), amount);
                System.out.println("Withdrawal successful.");
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
            System.out.println("Enter source account number:");
            String fromAccountNumber = scanner.nextLine();
            System.out.println("Enter destination account number:");
            String toAccountNumber = scanner.nextLine();
            System.out.println("Enter transfer amount:");
            double amount = scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Enter PIN for source account:");
            String sourcePin = scanner.nextLine();

            // Perform fund transfer
            accountService.transferFunds(fromAccountNumber, toAccountNumber, amount, sourcePin);
            System.out.println("Fund transfer successful.");
        } catch (InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Handle viewing transactions
    public void handleViewTransactions() {
        System.out.println("Enter account ID to view transactions:");
        int accountId = scanner.nextInt();

        transactionService.getAccountTransactionHistory(accountId).forEach(System.out::println);
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

    private String generateAccountNumber() {
        // Generate a 12-digit account number
        long number = (long) (Math.random() * 1_000_000_000_000L);
        return String.format("%012d", number);
    }
}
