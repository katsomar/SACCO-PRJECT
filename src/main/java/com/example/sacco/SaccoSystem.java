package com.example.sacco;

import datastructures.CustomHashMap;
import datastructures.CustomLinkedList;
import com.example.sacco.model.Transaction;

public class SaccoSystem {
    private CustomHashMap<String, User> users = new CustomHashMap<>();
    private CustomHashMap<String, CustomLinkedList<Transaction>> transactions = new CustomHashMap<>();

    public boolean signup(String userId, String name, String email, String password) {
        if (users.containsKey(userId)) {
            return false;
        }
        User user = new User(userId, name, email, password, 0.0);
        users.put(userId, user);
        transactions.put(userId, new CustomLinkedList<>());
        return true;
    }

    public User login(String userId, String password) {
        User user = users.get(userId);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean deposit(String userId, double amount) {
        User user = users.get(userId);
        if (user != null) {
            user.setBalance(user.getBalance() + amount);
            transactions.get(userId).add(new Transaction(userId, "Deposit", amount, java.time.LocalDateTime.now().toString()));
            return true;
        }
        return false;
    }

    public boolean withdraw(String userId, double amount) {
        User user = users.get(userId);
        if (user != null && user.getBalance() >= amount) {
            user.setBalance(user.getBalance() - amount);
            transactions.get(userId).add(new Transaction(userId, "Withdrawal", amount, java.time.LocalDateTime.now().toString()));
            return true;
        }
        return false;
    }

    public void printStatement(String userId) {
        CustomLinkedList<Transaction> userTransactions = transactions.get(userId);
        if (userTransactions != null) {
            System.out.println("Transaction Statement:");
            for (Transaction transaction : userTransactions) {
                System.out.println(transaction.getType() + ": " + transaction.getAmount());
            }
        } else {
            System.out.println("No transactions found.");
        }
    }

    public CustomLinkedList<Transaction> getTransactions(String userId, int n) {
        CustomLinkedList<Transaction> userTransactions = transactions.get(userId);
        CustomLinkedList<Transaction> result = new CustomLinkedList<>();
        int count = 0;
        for (int i = userTransactions.size() - 1; i >= 0 && count < n; i--) {
            result.add(userTransactions.get(i));
            count++;
        }
        return result;
    }

    public String formatCurrency(double amount) {
        return "UGX " + String.format("%,.2f", amount);
    }
}