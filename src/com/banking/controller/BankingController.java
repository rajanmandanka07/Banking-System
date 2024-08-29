package com.banking.controller;

import com.banking.exception.InsufficientFundsException;
import com.banking.model.Account;
import com.banking.model.User;
import com.banking.service.AccountService;
import com.banking.service.TransactionService;
import com.banking.service.UserService;

import java.util.Scanner;

public class BankingController {

    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final Scanner scanner;

    public BankingController() {
        this.userService = new UserService();
        this.accountService = new AccountService();
        this.transactionService = new TransactionService();
        this.scanner = new Scanner(System.in);
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

        User user = new User(name, email, phoneNumber, address);
        userService.registerUser(user);
//        System.out.println("User registered successfully.");
    }

    // Handle account creation
    public void handleAccountCreation() {
        System.out.println("Enter user ID for account creation:");
        int userId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("Enter account type (SAVINGS/CURRENT):");
        String accountType = scanner.nextLine();
        Account account = new Account(userId, generateAccountNumber(), userId, 0.0, accountType.toUpperCase(), new java.sql.Timestamp(System.currentTimeMillis()));

        accountService.createAccount(account);
        System.out.println("Account created successfully.");
    }

    // Handle deposit
    public void handleDeposit() {
        System.out.println("Enter account ID for deposit:");
        int accountId = scanner.nextInt();
        System.out.println("Enter deposit amount:");
        double amount = scanner.nextDouble();

        accountService.deposit(accountId, amount);
        System.out.println("Deposit successful.");
    }

    // Handle withdrawal
    public void handleWithdrawal() {
        try {
            System.out.println("Enter account ID for withdrawal:");
            int accountId = scanner.nextInt();
            System.out.println("Enter withdrawal amount:");
            double amount = scanner.nextDouble();

            accountService.withdraw(accountId, amount);
            System.out.println("Withdrawal successful.");
        } catch (InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Handle fund transfer
    public void handleFundTransfer() {
        try {
            System.out.println("Enter source account ID:");
            int fromAccountId = scanner.nextInt();
            System.out.println("Enter destination account ID:");
            int toAccountId = scanner.nextInt();
            System.out.println("Enter transfer amount:");
            double amount = scanner.nextDouble();

            accountService.transferFunds(fromAccountId, toAccountId, amount);
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

    private String generateAccountNumber() {
        // Generate a 12-digit account number (for example, using a simple random number generator)
        long number = (long) (Math.random() * 1_000_000_000_000L);
        return String.format("%012d", number);
    }
}
