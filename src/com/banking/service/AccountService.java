package com.banking.service;

import com.banking.dao.AccountDAO;
import com.banking.exception.InsufficientFundsException;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.util.InputValidator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountService {

    private final AccountDAO accountDAO;
    private final TransactionService transactionService;

    public AccountService() {
        this.accountDAO = new AccountDAO();
        this.transactionService = new TransactionService();
    }

    // Method to create a new account
    public boolean createAccount(Account account) {
        if (InputValidator.isValidAccount(account)) {
            try {
                accountDAO.addAccount(account);
                return true;
            } catch (Exception e) {
                System.err.println("Failed to create account: " + e.getMessage());
            }
        } else {
            System.err.println("Invalid account data provided.");
        }
        return false;
    }

    // Method to delete an account by account number with password validation
    public boolean deleteAccountByNumber(String accountNumber, String password) {
        try {
            // Authenticate the account using account number and password
            Account account = authenticateAccountByPassword(accountNumber, password);

            if (account != null) {
                // If authentication is successful, delete the account
                transactionService.removeTransaction(account.getAccountId());
                accountDAO.deleteAccount(account.getAccountId());
                return true;
            } else {
                // If authentication fails, print an error message
                System.err.println("Account deletion failed: Invalid account number or password.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Failed to delete account: " + e.getMessage());
            return false;
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
                transactionService.recordTransaction(transaction);

                System.out.println("Deposit successful!");
                System.out.println(account);
            } else {
                System.err.println("Invalid deposit operation.");
            }
        } catch (Exception e) {
            System.err.println("Failed to deposit money: " + e.getMessage());
        }
    }

    // Method to withdraw money from an account
    public void withdraw(int accountId, double amount) {
        try {
            Account account = accountDAO.getAccountById(accountId);

            if (account != null && amount > 0) {
                if (account.getBalance() >= amount) {
                    account.setBalance(account.getBalance() - amount);
                    accountDAO.updateAccount(account);

                    // Record the transaction
                    Transaction transaction = new Transaction(accountId, account.getAccountNumber(), "WITHDRAWAL", amount);
                    transactionService.recordTransaction(transaction);

                    System.out.println("Withdrawal successful!");
                    System.out.println(account);
                } else {
                    throw new InsufficientFundsException("Insufficient funds for withdrawal.");
                }
            } else {
                System.err.println("Invalid withdrawal operation.");
            }
        } catch (Exception e) {
            System.err.println("Failed to withdraw money: " + e.getMessage());
        }
    }

    // Method to transfer money from an account to another account
    public void transferFunds(String fromAccountNumber, String toAccountNumber, double amount, String sourcePin) throws InsufficientFundsException {
        try {
            Account fromAccount = accountDAO.getAccountByNumber(fromAccountNumber);
            Account toAccount = accountDAO.getAccountByNumber(toAccountNumber);

            if (fromAccount != null && toAccount != null && amount > 0) {
                if (fromAccount.getPin().equals(sourcePin)) {
                    if (fromAccount.getBalance() >= amount) {
                        // Deduct from source account
                        fromAccount.setBalance(fromAccount.getBalance() - amount);
                        accountDAO.updateAccount(fromAccount);

                        // Add to destination account
                        toAccount.setBalance(toAccount.getBalance() + amount);
                        accountDAO.updateAccount(toAccount);

                        // Record the transactions
                        Transaction debitTransaction = new Transaction(fromAccount.getAccountId(), fromAccountNumber, "TRANSFER", amount);
//                        transactionDAO.addTransaction(debitTransaction);
                        transactionService.recordTransaction(debitTransaction);

                        Transaction creditTransaction = new Transaction(toAccount.getAccountId(), toAccountNumber, "TRANSFER", amount);
//                        transactionDAO.addTransaction(creditTransaction);
                        transactionService.recordTransaction(creditTransaction);

//                        System.out.println("Transfer successful!");
                    } else {
                        throw new InsufficientFundsException("Insufficient funds for transfer.");
                    }
                } else {
                    System.err.println("Invalid PIN for source account.");
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

    // Method to retrieve all accounts of user based on phone number
    public List<Account> getAccountsByPhoneNumber(String phoneNumber) {
        try {
            return accountDAO.getAccountsByPhoneNumber(phoneNumber);
        } catch (SQLException e) {
            System.err.println("Failed to retrieve accounts by phone number: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Method to authenticate an account with account number and password
    public Account authenticateAccountByPassword(String accountNumber, String password) {
        try {
            Account account = accountDAO.getAccountByNumber(accountNumber);
            if (account != null && account.getPassword().equals(password)) {
                return account;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.err.println("Failed to authenticate account by password: " + e.getMessage());
            return null;
        }
    }

    // Method to authenticate an account with account number and PIN
    public Account authenticateAccountByPin(String accountNumber, String pin) {
        try {
            Account account = accountDAO.getAccountByNumber(accountNumber);
            if (account != null && account.getPin().equals(pin)) {
                return account;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.err.println("Failed to authenticate account by PIN: " + e.getMessage());
            return null;
        }
    }

    // Method to get account by account number
    public Account getAccountByAccountNumber(String accountNumber) {
        try {
            return accountDAO.getAccountByNumber(accountNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
