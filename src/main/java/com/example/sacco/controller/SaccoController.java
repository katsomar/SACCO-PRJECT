package com.example.sacco.controller;

import com.example.sacco.model.Transaction;
import com.example.sacco.model.User;
import com.example.sacco.repository.TransactionRepository;
import com.example.sacco.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

                List<Transaction> transactions = transactionRepository.findByUserUserIdOrderByTimestampDesc(user.getUserId());

                model.addAttribute("transactions", transactions);

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

        List<Transaction> transactions = transactionRepository.findByUserUserIdOrderByTimestampDesc(userId);

        model.addAttribute("transactions", transactions);

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

                List<Transaction> transactions = transactionRepository.findByUserUserIdOrderByTimestampDesc(userId);

                model.addAttribute("transactions", transactions);
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

        List<Transaction> transactions = transactionRepository.findByUserUserIdOrderByTimestampDesc(userId);

        model.addAttribute("transactions", transactions);

        return "dashboard"; // Return updated dashboard view
    }

    @GetMapping("/transactions")
    public String getTransactions(@RequestParam String userId, Model model) {
        List<Transaction> transactions = transactionRepository.findByUserUserIdOrderByTimestampDesc(userId);
        model.addAttribute("transactions", transactions);
        return "transactions"; // Serve the transactions page
    }
}
