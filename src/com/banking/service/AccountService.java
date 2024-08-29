package com.banking.service;

import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.exception.InsufficientFundsException;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.util.InputValidator;

import java.sql.SQLException;
import java.util.List;

public class AccountService {

    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
    }

    // Method to create a new account
    public void createAccount(Account account) {
        if (InputValidator.isValidAccount(account)) {
            try {
                accountDAO.addAccount(account);
                System.out.println("Account created successfully!");
            } catch (Exception e) {
                System.err.println("Failed to create account: " + e.getMessage());
            }
        } else {
            System.err.println("Invalid account data provided.");
        }
    }

    // Method to close an account
    public void closeAccount(int accountId) {
        try {
            Account existingAccount = accountDAO.getAccountById(accountId);
            if (existingAccount != null) {
                accountDAO.deleteAccount(accountId);
                System.out.println("Account closed successfully!");
            } else {
                System.err.println("Account not found with ID: " + accountId);
            }
        } catch (Exception e) {
            System.err.println("Failed to close account: " + e.getMessage());
        }
    }

    // Method to deposit money into an account
    public void deposit(int accountId, double amount) {
        try {
            Account account = accountDAO.getAccountById(accountId);
            if (account != null && amount > 0) {
                account.setBalance(account.getBalance() + amount);
                accountDAO.updateAccount(account);

                // Record the transaction
                Transaction transaction = new Transaction(accountId, account.getAccountNumber(), "DEPOSIT", amount);
                transactionDAO.addTransaction(transaction);

                System.out.println("Deposit successful!");
            } else {
                System.err.println("Invalid deposit operation.");
            }
        } catch (Exception e) {
            System.err.println("Failed to deposit money: " + e.getMessage());
        }
    }

    // Method to withdraw money from an account
    public void withdraw(int accountId, double amount) throws InsufficientFundsException {
        try {
            Account account = accountDAO.getAccountById(accountId);
            if (account != null && amount > 0) {
                if (account.getBalance() >= amount) {
                    account.setBalance(account.getBalance() - amount);
                    accountDAO.updateAccount(account);

                    // Record the transaction
                    Transaction transaction = new Transaction(accountId, account.getAccountNumber(), "WITHDRAWAL", amount);
                    transactionDAO.addTransaction(transaction);

                    System.out.println("Withdrawal successful!");
                } else {
                    throw new InsufficientFundsException("Insufficient funds for withdrawal.");
                }
            } else {
                System.err.println("Invalid withdrawal operation.");
            }
        } catch (InsufficientFundsException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Failed to withdraw money: " + e.getMessage());
        }
    }

    // Method to transfer funds between accounts
    public void transferFunds(int fromAccountId, int toAccountId, double amount) throws InsufficientFundsException {
        try {
            Account fromAccount = accountDAO.getAccountById(fromAccountId);
            Account toAccount = accountDAO.getAccountById(toAccountId);

            if (fromAccount != null && toAccount != null && amount > 0) {
                if (fromAccount.getBalance() >= amount) {
                    // Deduct from source account
                    fromAccount.setBalance(fromAccount.getBalance() - amount);
                    accountDAO.updateAccount(fromAccount);

                    // Add to destination account
                    toAccount.setBalance(toAccount.getBalance() + amount);
                    accountDAO.updateAccount(toAccount);

                    // Record the transactions
                    Transaction debitTransaction = new Transaction(fromAccountId, fromAccount.getAccountNumber(), "TRANSFER", amount);
                    transactionDAO.addTransaction(debitTransaction);

                    Transaction creditTransaction = new Transaction(toAccountId, toAccount.getAccountNumber(), "TRANSFER", amount);
                    transactionDAO.addTransaction(creditTransaction);

                    System.out.println("Transfer successful!");
                } else {
                    throw new InsufficientFundsException("Insufficient funds for transfer.");
                }
            } else {
                System.err.println("Invalid transfer operation.");
            }
        } catch (InsufficientFundsException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Failed to transfer funds: " + e.getMessage());
        }
    }

    // Method to retrieve all accounts (for debugging or administrative purposes)
    public List<Account> getAllAccounts() {
        try {
            return accountDAO.getAllAccounts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
