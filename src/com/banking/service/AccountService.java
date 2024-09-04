package com.banking.service;

import com.banking.dao.UserDAO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.banking.dao.AccountDAO;
import com.banking.exception.InsufficientFundsException;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.User;
import com.banking.util.InputValidator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountService {

    private final AccountDAO accountDAO;
    private final UserDAO userDAO;
    private final TransactionService transactionService;

    public AccountService() {
        this.accountDAO = new AccountDAO();
        this.userDAO = new UserDAO();
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

    // Method to Download Transaction History as PDF
    public void downloadTransactions(String accountNumber) {
        // Retrieve account details
        Account account = getAccountByAccountNumber(accountNumber);

        if (account == null) {
            System.out.println("Account not found!");
            return;
        }

        // Retrieve user details
        User user;
        try {
            user = userDAO.getUserById(account.getUserId());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error occurred while retrieving user details.");
            return;
        }

        // Retrieve transaction history
        List<Transaction> transactions = transactionService.getAccountTransactionHistory(accountNumber);

        // Define the output PDF file name
        String fileName = "Transaction_History_" + accountNumber + ".pdf";

        // Create a new document
        Document document = new Document();

        try {
            // Create a PDF writer instance
            PdfWriter.getInstance(document, new FileOutputStream(fileName));

            // Open the document for writing
            document.open();

            // Main Header
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph header = new Paragraph("Transaction History Report", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(new Paragraph(" "));

            // Set font for the headers
            headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            // Create a table with 2 columns for side-by-side details
            PdfPTable detailsTable = new PdfPTable(2);
            detailsTable.setWidthPercentage(100); // Set table width to 100% of the page

            // Set a fixed height for the cells
            float fixedHeaderCellHeight = 25f; // Adjust this value as needed
            float fixedDataCellHeight = 20f;

            // Add user details header to the table
            PdfPCell userHeader = new PdfPCell(new Phrase("User Details:", headerFont));
            userHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            userHeader.setFixedHeight(fixedHeaderCellHeight);
            detailsTable.addCell(userHeader);

            // Add account details header to the table
            PdfPCell accountHeader = new PdfPCell(new Phrase("Account Details:", headerFont));
            accountHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            accountHeader.setFixedHeight(fixedHeaderCellHeight);
            detailsTable.addCell(accountHeader);

            // Set font for the data
            Font dataFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

            // Add user details to the table with fixed cell height
            PdfPCell cell1 = new PdfPCell(new Phrase("Name: " + user.getName(), dataFont));
            cell1.setFixedHeight(fixedDataCellHeight);
            detailsTable.addCell(cell1);

            PdfPCell cell2 = new PdfPCell(new Phrase("Account Number: " + account.getAccountNumber(), dataFont));
            cell2.setFixedHeight(fixedDataCellHeight);
            detailsTable.addCell(cell2);

            PdfPCell cell3 = new PdfPCell(new Phrase("Email: " + user.getEmail(), dataFont));
            cell3.setFixedHeight(fixedDataCellHeight);
            detailsTable.addCell(cell3);

            PdfPCell cell4 = new PdfPCell(new Phrase("Account Type: " + account.getAccountType(), dataFont));
            cell4.setFixedHeight(fixedDataCellHeight);
            detailsTable.addCell(cell4);

            PdfPCell cell5 = new PdfPCell(new Phrase("Phone Number: " + user.getPhoneNumber(), dataFont));
            cell5.setFixedHeight(fixedDataCellHeight);
            detailsTable.addCell(cell5);

            PdfPCell cell6 = new PdfPCell(new Phrase("Balance: " + account.getBalance(), dataFont));
            cell6.setFixedHeight(fixedDataCellHeight);
            detailsTable.addCell(cell6);

            PdfPCell cell7 = new PdfPCell(new Phrase("Address: " + (user.getAddress() != null ? user.getAddress() : "N/A"), dataFont));
            cell7.setFixedHeight(fixedDataCellHeight);
            detailsTable.addCell(cell7);

            PdfPCell cell8 = new PdfPCell(new Phrase("Created At: " + account.getCreatedAt(), dataFont));
            cell8.setFixedHeight(fixedDataCellHeight);
            detailsTable.addCell(cell8);

            // Add the table to the document
            document.add(detailsTable);

            // Add a blank line after the table
            document.add(new Paragraph(" "));

            // Add transaction history in table format
            document.add(new Paragraph("Transaction History:", headerFont));
            document.add(new Paragraph(" ")); // Add a blank line

            // Create a table with 4 columns (ID, Type, Amount, Date)
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(90);

            // Set the relative widths of each column (e.g., 20%, 30%, 20%, 30%)
            float[] columnWidths = {2f, 3f, 2f, 3f};
            table.setWidths(columnWidths);

            // Set the headers for the table
            PdfPCell header1 = new PdfPCell(new Phrase("Transaction ID", headerFont));
            header1.setHorizontalAlignment(Element.ALIGN_CENTER);
            header1.setFixedHeight(fixedHeaderCellHeight); // Set the height of the header cell
            table.addCell(header1);

            PdfPCell header2 = new PdfPCell(new Phrase("Transaction Type", headerFont));
            header2.setHorizontalAlignment(Element.ALIGN_CENTER);
            header2.setFixedHeight(fixedHeaderCellHeight); // Set the height of the header cell
            table.addCell(header2);

            PdfPCell header3 = new PdfPCell(new Phrase("Amount", headerFont));
            header3.setHorizontalAlignment(Element.ALIGN_CENTER);
            header3.setFixedHeight(fixedHeaderCellHeight); // Set the height of the header cell
            table.addCell(header3);

            PdfPCell header4 = new PdfPCell(new Phrase("Date", headerFont));
            header4.setHorizontalAlignment(Element.ALIGN_CENTER);
            header4.setFixedHeight(fixedHeaderCellHeight); // Set the height of the header cell
            table.addCell(header4);

            // Add transaction data to the table with centered alignment
            for (Transaction transaction : transactions) {
                PdfPCell cell9 = new PdfPCell(new Phrase(String.valueOf(transaction.getTransactionId()), dataFont));
                cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell9.setFixedHeight(fixedDataCellHeight); // Set the height of the data cell
                table.addCell(cell9);

                PdfPCell cell10 = new PdfPCell(new Phrase(transaction.getTransactionType(), dataFont));
                cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell10.setFixedHeight(fixedDataCellHeight); // Set the height of the data cell
                table.addCell(cell10);

                PdfPCell cell11 = new PdfPCell(new Phrase(String.valueOf(transaction.getAmount()), dataFont));
                cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell11.setFixedHeight(fixedDataCellHeight); // Set the height of the data cell
                table.addCell(cell11);

                PdfPCell cell12 = new PdfPCell(new Phrase(String.valueOf(transaction.getTransactionDate()), dataFont));
                cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell12.setFixedHeight(fixedDataCellHeight); // Set the height of the data cell
                table.addCell(cell12);
            }

            // Add the table to the document
            document.add(table);

            // Close the document
            document.close();

            System.out.println("Transaction history downloaded successfully as " + fileName);
            System.out.println("File saved at: " + new File(fileName).getAbsolutePath());

        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
            System.out.println("An error occurred while downloading the transaction history.");
        }
    }
}
