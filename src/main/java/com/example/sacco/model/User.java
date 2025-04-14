package com.example.sacco.model;

import jakarta.persistence.*;
import datastructures.CustomStack; // Correct import
import datastructures.CustomLinkedList; // Correct import
import java.util.List;

@Entity
@Table(name = "users") // Rename the table to "users"
public class User {
    @Id
    private String userId;
    private String name;
    private String email;
    private String password;
    private double balance;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private CustomLinkedList<Transaction> transactions = new CustomLinkedList<>(); // Replace ArrayList with CustomLinkedList

    private CustomStack<Transaction> transactionStack = new CustomStack<>(); // Add CustomStack for undo feature

    // Default constructor (required by JPA)
    public User() {}

    // Constructor to match the parameters
    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public CustomLinkedList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(CustomLinkedList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void deposit(double amount) { // Add transaction to CustomLinkedList
        balance += amount;
        Transaction transaction = new Transaction(this, "Deposit", amount);
        transactions.add(transaction); // Add transaction to CustomLinkedList
        transactionStack.push(transaction); // Push transaction onto stack
    }

    public boolean withdraw(double amount) { // Add transaction to CustomLinkedList
        if (balance >= amount) {
            balance -= amount;
            Transaction transaction = new Transaction(this, "Withdrawal", amount);
            transactions.add(transaction); // Add transaction to CustomLinkedList
            transactionStack.push(transaction); // Push transaction onto stack
            return true;
        }
        return false;
    }

    public void undoLastTransaction() {
        if (!transactionStack.isEmpty()) {
            Transaction lastTransaction = transactionStack.pop(); // Pop the last transaction
            if (lastTransaction.getType().equals("Deposit")) {
                balance -= lastTransaction.getAmount();
            } else if (lastTransaction.getType().equals("Withdrawal")) {
                balance += lastTransaction.getAmount();
            }
            transactions.remove(lastTransaction); // Remove from transaction history
        }
    }
}