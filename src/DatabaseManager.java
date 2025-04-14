package com.example.sacco;

import java.sql.*;
import datastructures.CustomLinkedList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:database.db";
    // Username and password are not needed with SQLite.

    public DatabaseManager() {
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        createTables();
    }

    private void createTables() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String createUsersTable = "CREATE TABLE IF NOT EXISTS Users ("
                    + "userId TEXT PRIMARY KEY, "
                    + "name TEXT NOT NULL, "
                    + "email TEXT NOT NULL, "
                    + "password TEXT NOT NULL, "
                    + "balance REAL DEFAULT 0.0"
                    + ");";
            stmt.execute(createUsersTable);

            String createTransactionsTable = "CREATE TABLE IF NOT EXISTS Transactions ("
                    + "transactionId INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "userId TEXT NOT NULL, "
                    + "type TEXT NOT NULL, "
                    + "amount REAL NOT NULL, "
                    + "timestamp TEXT NOT NULL, "
                    + "FOREIGN KEY(userId) REFERENCES Users(userId)"
                    + ");";
            stmt.execute(createTransactionsTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean insertUser(String userId, String name, String email, String password) {
        String sql = "INSERT INTO Users(userId, name, email, password) VALUES(?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public User getUser(String userId, String password) {
        String sql = password == null
                ? "SELECT * FROM Users WHERE userId = ?"
                : "SELECT * FROM Users WHERE userId = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            if (password != null) {
                pstmt.setString(2, password);
            }
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("userId"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getDouble("balance")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateBalance(String userId, double balance) {
        String sql = "UPDATE Users SET balance = ? WHERE userId = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, balance);
            pstmt.setString(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertTransaction(String userId, String type, double amount) {
        String sql = "INSERT INTO Transactions(userId, type, amount, timestamp) VALUES(?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, type);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, java.time.LocalDateTime.now().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CustomLinkedList<Transaction> getLastNTransactions(String userId, int n) {
        String sql = "SELECT * FROM Transactions WHERE userId = ? ORDER BY timestamp DESC LIMIT ?";
        CustomLinkedList<Transaction> transactions = new CustomLinkedList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setInt(2, n);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getString("userId"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getString("timestamp")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();
        dbManager.insertUser("user1", "John Doe", "john.doe@example.com", "password123");
    }
}
