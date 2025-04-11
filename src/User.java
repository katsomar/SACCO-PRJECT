package com.example.sacco.model;

import jakarta.persistence.*;
import datastructures.CustomLinkedList;
import java.util.List;

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

    public CustomLinkedList<Transaction> getTransactions() {
        return transactions;
    }

    public void deposit(double amount) {
        balance += amount;
        // Adding a new transaction to the CustomLinkedList
        Transaction transaction = new Transaction(userId, "Deposit", amount);
        transactions.add(transaction);
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            // Adding a new transaction to the CustomLinkedList
            Transaction transaction = new Transaction(userId, "Withdrawal", amount);
            transactions.add(transaction);
            return true;
        }
        return false;
    }

    public List<Transaction> getLastNTransactions(int n) {
        // Using CustomLinkedList to retrieve the last N transactions
        List<Transaction> recentTransactions = new CustomLinkedList<>();
        int count = 0;

        for (int i = transactions.size() - 1; i >= 0 && count < n; i--) {
            recentTransactions.add(transactions.get(i));
            count++;
        }

        return recentTransactions;
    }
}
