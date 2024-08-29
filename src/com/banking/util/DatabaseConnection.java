package com.banking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection connection;

    private DatabaseConnection() {
        // Private constructor to prevent instantiation
    }

    public static Connection getConnection() {
        if (connection == null) {
            synchronized (DatabaseConnection.class) {
                if (connection == null) {
                    try {
                        // Replace with your database credentials and URL
                        String url = "jdbc:mysql://localhost:3306/BankingSystemDB";
                        String user = "root";
                        String password = "Rajan@07";

                        // Load the MySQL driver
                        Class.forName("com.mysql.cj.jdbc.Driver");

                        // Establish the connection
                        connection = DriverManager.getConnection(url, user, password);
//                        System.out.println("Database connection established.");
                    } catch (ClassNotFoundException | SQLException e) {
                        System.err.println("Database connection failed: " + e.getMessage());
                    }
                }
            }
        }
        return connection;
    }
}
