package com.example.sacco;

import jakarta.persistence.*;
import datastructures.CustomLinkedList;
import com.example.sacco.model.Transaction;  // added import

@Entity
public class User {
    @Id
    private String userId;
    private String name;
    private String email;
    private String password;
    private double balance;

    // Using CustomLinkedList to store transactions for this user
    // CustomLinkedList is chosen because it allows efficient insertion and removal of transactions
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private CustomLinkedList<Transaction> transactions = new CustomLinkedList<>();

    public User() {}

    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.balance = 0.0;
    }

    public User(String userId, String name, String email, String password, double balance) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }


    // Getters and Setters
    public String getUserId() {
        return userId;
    }
    

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }
    
    // Added setter method for balance
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public CustomLinkedList<Transaction> getTransactions() {
        return transactions;
    }

    public void deposit(double amount) {
        balance += amount;
        // Updated constructor call with timestamp argument
        Transaction transaction = new Transaction(userId, "Deposit", amount, java.time.LocalDateTime.now().toString());
        transactions.add(transaction);
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            // Updated constructor call with timestamp argument
            Transaction transaction = new Transaction(userId, "Withdrawal", amount, java.time.LocalDateTime.now().toString());
            transactions.add(transaction);
            return true;
        }
        return false;
    }

    public CustomLinkedList<Transaction> getLastNTransactions(int n) {
        // Using CustomLinkedList to retrieve the last N transactions
        CustomLinkedList<Transaction> recentTransactions = new CustomLinkedList<>();
        int count = 0;

        for (int i = transactions.size() - 1; i >= 0 && count < n; i--) {
            recentTransactions.add(transactions.get(i));
            count++;
        }

        return recentTransactions;
    }
}
