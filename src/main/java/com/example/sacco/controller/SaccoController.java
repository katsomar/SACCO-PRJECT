package com.example.sacco.controller;

import com.example.sacco.model.Transaction;
import com.example.sacco.model.User;
import com.example.sacco.repository.TransactionRepository;
import com.example.sacco.repository.UserRepository;
import datastructures.CustomHashMap; // Ensure this import is resolved
import datastructures.CustomLinkedList; // Ensure this import is resolved
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api")
public class SaccoController {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public SaccoController(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // Serve the signup.html page
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String userId, @RequestParam String name,
                         @RequestParam String email, @RequestParam String password, Model model) {
        if (userRepository.existsById(userId)) {
            model.addAttribute("error", "User ID already exists.");
            return "signup"; // Redirect back to the signup page with an error message
        }
        User user = new User(userId, name, email, password);
        userRepository.save(user);
        model.addAttribute("success", "Signup successful! Please log in.");
        return "redirect:/"; // Redirect to the login page
    }

    @PostMapping("/login")
    public String login(@RequestParam String userId, @RequestParam String password, Model model) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getPassword().equals(password)) {
                model.addAttribute("user", user);
                model.addAttribute("balance", user.getBalance());
                return "dashboard"; // Render dashboard.html
            } else {
                model.addAttribute("error", "Wrong password!");
                return "redirect:/index.html"; // Redirect to login page with error message
            }
        } else {
            model.addAttribute("error", "User not found!");
            return "redirect:/index.html";
        }
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam String userId, @RequestParam double amount, Model model) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            model.addAttribute("error", "User not found.");
            return "dashboard"; // Redirect back to the dashboard with an error message
        }
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);

        Transaction transaction = new Transaction(user, "Deposit", amount);
        transactionRepository.save(transaction);

        model.addAttribute("success", "Deposit successful!");
        model.addAttribute("user", user);
        model.addAttribute("balance", user.getBalance());

        return "dashboard"; // Redirect back to the dashboard with updated data
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String userId, @RequestParam double amount, Model model) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getBalance() < amount) {
            model.addAttribute("error", "Insufficient balance or user not found.");
            // Ensure required attributes are set so dashboard.html can render properly:
            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("balance", user.getBalance());
            }
            return "dashboard"; // Return the dashboard view with the error message
        }
        // Process withdrawal if balance is sufficient
        user.setBalance(user.getBalance() - amount);
        userRepository.save(user);

        Transaction transaction = new Transaction(user, "Withdrawal", amount);
        transactionRepository.save(transaction);

        model.addAttribute("success", "Withdrawal successful!");
        model.addAttribute("user", user);
        model.addAttribute("balance", user.getBalance());

        return "dashboard"; // Return updated dashboard view
    }

    @GetMapping("/transactions")
    public String getTransactions(@RequestParam String userId,
                                  @RequestParam(required = false) String searchType,
                                  @RequestParam(required = false) String searchDate,
                                  Model model) {
        List<Transaction> transactions = transactionRepository.findByUserUserIdOrderByTimestampDesc(userId);

        // Replace HashMap with CustomHashMap
        CustomHashMap<LocalDate, CustomLinkedList<Transaction>> transactionMap = new CustomHashMap<>();
        for (Transaction transaction : transactions) {
            LocalDate date = transaction.getTimestamp().toLocalDate();
            if (!transactionMap.containsKey(date)) {
                transactionMap.put(date, new CustomLinkedList<>());
            }
            transactionMap.get(date).add(transaction);
        }

        // Filter transactions by date if searchDate is provided
        if (searchDate != null && !searchDate.isEmpty()) {
            LocalDate date = LocalDate.parse(searchDate);
            transactions = transactionMap.containsKey(date) ? transactionMap.get(date) : new CustomLinkedList<>();
        }

        // Further filter transactions by type if searchType is provided
        if (searchType != null && !searchType.isEmpty()) {
            CustomLinkedList<Transaction> filteredTransactions = new CustomLinkedList<>();
            for (Transaction transaction : transactions) {
                if (transaction.getType().equalsIgnoreCase(searchType)) {
                    filteredTransactions.add(transaction);
                }
            }
            transactions = filteredTransactions;
        }

        model.addAttribute("transactions", transactions);
        model.addAttribute("userId", userId);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchDate", searchDate);
        return "transactions";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @RequestParam String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("user", user);
            model.addAttribute("balance", user.getBalance());
            return "dashboard"; // Render the dashboard page
        } else {
            model.addAttribute("error", "User not found!");
            return "redirect:/index.html";
        }
    }
}
