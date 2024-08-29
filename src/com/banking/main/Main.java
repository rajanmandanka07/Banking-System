package com.banking.main;

import com.banking.controller.BankingController;
import com.banking.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = DatabaseConnection.getConnection();
        BankingController controller = new BankingController();

        System.out.println("Welcome to the Banking System");

        boolean running = true;

        while (running) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Register a New User");
            System.out.println("2. Existing User");
            System.out.println("3. Exit");
            System.out.print("Please choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    controller.handleUserRegistration();
                    break;
                case 2:
                    existingUserMenu(scanner, controller);
                    break;
                case 3:
                    running = false;
                    System.out.println("Thank you for using the Banking System!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        // Close the connection when the application exits
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
//                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close database connection: " + e.getMessage());
        }

        scanner.close();
    }

    private static void existingUserMenu(Scanner scanner, BankingController controller) {
        boolean running = true;

        while (running) {
            System.out.println("\nExisting User Menu:");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit to Main Menu");
            System.out.print("Please choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    controller.handleAccountCreation();
                    break;
                case 2:
                    loggedInMenu(scanner, controller);
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void loggedInMenu(Scanner scanner, BankingController controller) {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\nUser Menu:");
            System.out.println("1. Deposit Funds");
            System.out.println("2. Withdraw Funds");
            System.out.println("3. Transfer Funds");
            System.out.println("4. View Transaction History");
            System.out.println("5. Exit to Existing User Menu");
            System.out.print("Please choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    controller.handleDeposit();
                    break;
                case 2:
                    controller.handleWithdrawal();
                    break;
                case 3:
                    controller.handleFundTransfer();
                    break;
                case 4:
                    controller.handleViewTransactions();
                    break;
                case 5:
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
