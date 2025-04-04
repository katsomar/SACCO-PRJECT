// Removed package declaration to match the directory structure

import java.util.List;

public class SaccoSystem {
    private DatabaseManager dbManager;

    public SaccoSystem() {
        this.dbManager = new DatabaseManager();
    }

    public boolean signup(String userId, String name, String email, String password) {
        return dbManager.insertUser(userId, name, email, password);
    }

    public User login(String userId, String password) {
        return dbManager.getUser(userId, password);
    }

    public boolean deposit(String userId, double amount) {
        User user = dbManager.getUser(userId, null);
        if (user != null) {
            double newBalance = user.getBalance() + amount;
            dbManager.updateBalance(userId, newBalance);
            dbManager.insertTransaction(userId, "Deposit", amount);
            return true;
        }
        return false;
    }

    public boolean withdraw(String userId, double amount) {
        User user = dbManager.getUser(userId, null);
        if (user != null && user.getBalance() >= amount) {
            double newBalance = user.getBalance() - amount;
            dbManager.updateBalance(userId, newBalance);
            dbManager.insertTransaction(userId, "Withdrawal", amount);
            return true;
        }
        return false;
    }

    public void printStatement(String userId) {
        User user = dbManager.getUser(userId, null);
        if (user != null) {
            System.out.println("Transaction Statement for " + user.getName());
            List<Transaction> transactions = dbManager.getLastNTransactions(userId, 10); // Retrieve last 10 transactions
            for (Transaction t : transactions) {
                System.out.println(t.getTimestamp() + " - " + t.getType() + ": " + formatCurrency(t.getAmount()));
            }
        }
    }

    public List<Transaction> getTransactions(String userId, int n) {
        return dbManager.getLastNTransactions(userId, n);
    }

    public String formatCurrency(double amount) {
        return "UGX " + String.format("%,.2f", amount);
    }
}
