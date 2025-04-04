package com.example.sacco.model;

import jakarta.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class User {
    @Id
    private String userId;
    private String name;
    private String email;
    private String password;
    private double balance;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new LinkedList<>();

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

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void deposit(double amount) {
        balance += amount;
        Transaction transaction = new Transaction(userId, "Deposit", amount);
        transactions.add(transaction);
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            Transaction transaction = new Transaction(userId, "Withdrawal", amount);
            transactions.add(transaction);
            return true;
        }
        return false;
    }

    public List<Transaction> getLastNTransactions(int n) {
        List<Transaction> recentTransactions = new LinkedList<>();
        int count = 0;

        for (int i = transactions.size() - 1; i >= 0 && count < n; i--) {
            recentTransactions.add(transactions.get(i));
            count++;
        }

        return recentTransactions;
    }
}
