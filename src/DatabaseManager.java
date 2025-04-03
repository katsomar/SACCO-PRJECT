import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    // Update connection parameters for MS SQL Server:
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=SaccoDB";
    private static final String DB_USER = "sa"; // Replace with your SQL Server username
    private static final String DB_PASSWORD = "omar1234"; // Replace with your SQL Server password

    public DatabaseManager() {
        try {
            // Load the SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("SQL Server JDBC driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server JDBC driver not found. Make sure it's in the classpath.");
            e.printStackTrace();
        }

        // Debug: Check if the database connection is successful
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            if (conn != null) {
                System.out.println("Database connection established at: " + DB_URL);
            }
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
            e.printStackTrace();
        }

        createTables();
    }

    private void createTables() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            // Create Users table using T-SQL syntax
            String createUsersTable =
                "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Users' AND xtype='U') " +
                "BEGIN " +
                "CREATE TABLE Users (" +
                "userId NVARCHAR(50) PRIMARY KEY, " +
                "name NVARCHAR(100) NOT NULL, " +
                "email NVARCHAR(100) NOT NULL, " +
                "password NVARCHAR(100) NOT NULL, " +
                "balance FLOAT DEFAULT 0.0" +
                "); " +
                "END";
            stmt.execute(createUsersTable);

            // Create Transactions table using T-SQL syntax
            String createTransactionsTable =
                "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Transactions' AND xtype='U') " +
                "BEGIN " +
                "CREATE TABLE Transactions (" +
                "transactionId INT IDENTITY(1,1) PRIMARY KEY, " +
                "userId NVARCHAR(50) NOT NULL, " +
                "type NVARCHAR(50) NOT NULL, " +
                "amount FLOAT NOT NULL, " +
                "timestamp DATETIME NOT NULL, " +
                "FOREIGN KEY(userId) REFERENCES Users(userId)" +
                "); " +
                "END";
            stmt.execute(createTransactionsTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean insertUser(String userId, String name, String email, String password) {
        String sql = "INSERT INTO Users(userId, name, email, password) VALUES(?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
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
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
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
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
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
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
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

    public List<Transaction> getLastNTransactions(String userId, int n) {
        String sql = "SELECT * FROM Transactions WHERE userId = ? ORDER BY timestamp DESC LIMIT ?";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
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
        // Example usage of dbManager to insert a user
        dbManager.insertUser("user1", "John Doe", "john.doe@example.com", "password123");
        System.out.println("Database setup complete.");
    }
}
