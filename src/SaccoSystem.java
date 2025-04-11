// Importing List to handle collections of transactions
import java.util.List;
import datastructures.CustomQueue;

public class SaccoSystem {
    private DatabaseManager dbManager;
    private CustomQueue<Transaction> transactionQueue = new CustomQueue<>(); // Add CustomQueue for transactions

    public SaccoSystem() { //constructor that initializes saccosystem object
        this.dbManager = new DatabaseManager();//creates an instance of the DatabaseManager class and assigns it to db ,manager
    }

    //registering new user 
    public boolean signup(String userId, String name, String email, String password) {
        return dbManager.insertUser(userId, name, email, password);
    }

    //login method that checks if the user exists in the database and returns the user object if found
    //otherwise returns null
    public User login(String userId, String password) {
        return dbManager.getUser(userId, password);
    }

    public boolean deposit(String userId, double amount) {
        User user = dbManager.getUser(userId, null);//fetch user details
        if (user != null) {
            double newBalance = user.getBalance() + amount;
            dbManager.updateBalance(userId, newBalance);
            dbManager.insertTransaction(userId, "Deposit", amount);//recordes deposit 
            return true;
        }
        return false;
    }
    //checks if the user has sufficient funds to withdraw the amount required and then updates the balance, and stores the transaction.
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
    
    //prints the transaction statement for a user if the user exists in the database
    public void printStatement(String userId) {
        User user = dbManager.getUser(userId, null);
        if (user != null) {
            System.out.println("Transaction Statement for " + user.getName());
            // Using List to retrieve and iterate over the last 10 transactions
            List<Transaction> transactions = dbManager.getLastNTransactions(userId, 10); // Retrieve last 10 transactions, iterates over transactions and prints them
            for (Transaction t : transactions) {
                System.out.println(t.getTimestamp() + " - " + t.getType() + ": " + formatCurrency(t.getAmount()));
            }
        }
    }
    //gets the last n transactions for a user
    public List<Transaction> getTransactions(String userId, int n) {
        // Returning a List of transactions for the user
        return dbManager.getLastNTransactions(userId, n);//returns the list of transacion objects
    }

    //currency formatting
    public String formatCurrency(double amount) {
        return "UGX " + String.format("%,.2f", amount);
    }

    public void processTransactions() {
        while (!transactionQueue.isEmpty()) {
            Transaction transaction = transactionQueue.dequeue(); // Dequeue transaction
            dbManager.insertTransaction(transaction.getUser().getUserId(), transaction.getType(), transaction.getAmount());
        }
    }

    public void addTransactionToQueue(Transaction transaction) {
        transactionQueue.enqueue(transaction); // Enqueue transaction
    }
}
