import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SaccoGUI {
    private SaccoSystem saccoSystem;

    public SaccoGUI() {
        saccoSystem = new SaccoSystem();
        showLoginScreen();
    }

    private void showLoginScreen() {
        JFrame frame = new JFrame("SACCO Management System - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel header = new JLabel("Welcome to SACCO Management System", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setForeground(new Color(0, 102, 204));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel userIdLabel = new JLabel("User ID:");
        JTextField userIdField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Signup");

        formPanel.add(userIdLabel);
        formPanel.add(userIdField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(loginButton);
        formPanel.add(signupButton);

        panel.add(header, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);

        frame.add(panel);
        frame.setVisible(true);

        loginButton.addActionListener(e -> {
            String userId = userIdField.getText();
            String password = new String(passwordField.getPassword());
            User user = saccoSystem.login(userId, password);
            if (user != null) {
                frame.dispose();
                showDashboard(user);
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        signupButton.addActionListener(e -> {
            frame.dispose();
            showSignupScreen();
        });
    }

    private void showSignupScreen() {
        JFrame frame = new JFrame("SACCO Management System - Signup");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel header = new JLabel("Signup for a New Account", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setForeground(new Color(0, 102, 204));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel userIdLabel = new JLabel("User ID:");
        JTextField userIdField = new JTextField();
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton signupButton = new JButton("Signup");
        JButton backButton = new JButton("Back");

        formPanel.add(userIdLabel);
        formPanel.add(userIdField);
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(signupButton);
        formPanel.add(backButton);

        panel.add(header, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);

        frame.add(panel);
        frame.setVisible(true);

        signupButton.addActionListener(e -> {
            String userId = userIdField.getText();
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            if (saccoSystem.signup(userId, name, email, password)) {
                JOptionPane.showMessageDialog(frame, "Signup successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                showLoginScreen();
            } else {
                JOptionPane.showMessageDialog(frame, "User ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            showLoginScreen();
        });
    }

    private void showDashboard(User user) {
        JFrame frame = new JFrame("SACCO Management System - Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel header = new JLabel("Dashboard", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setForeground(new Color(0, 102, 204));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));

        JLabel balanceLabel = new JLabel("Balance: " + saccoSystem.formatCurrency(user.getBalance()), JLabel.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        balanceLabel.setForeground(new Color(34, 139, 34));

        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton transactionsButton = new JButton("View Transactions");
        JButton logoutButton = new JButton("Logout");

        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(transactionsButton);
        buttonPanel.add(logoutButton);

        panel.add(header, BorderLayout.NORTH);
        panel.add(balanceLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);

        final User[] userWrapper = {user};
        depositButton.addActionListener(e -> {
            String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to deposit:");
            if (amountStr != null) {
                try {
                    double amount = Double.parseDouble(amountStr);
                    if (saccoSystem.deposit(userWrapper[0].getUserId(), amount)) {
                        JOptionPane.showMessageDialog(frame, "Deposit successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        userWrapper[0] = saccoSystem.login(userWrapper[0].getUserId(), userWrapper[0].getPassword());
                        balanceLabel.setText("Balance: " + saccoSystem.formatCurrency(userWrapper[0].getBalance()));
                    } else {
                        JOptionPane.showMessageDialog(frame, "Deposit failed! User not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid amount! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        withdrawButton.addActionListener(e -> {
            String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to withdraw:");
            if (amountStr != null) {
                try {
                    double amount = Double.parseDouble(amountStr);
                    if (saccoSystem.withdraw(userWrapper[0].getUserId(), amount)) {
                        JOptionPane.showMessageDialog(frame, "Withdrawal successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        userWrapper[0] = saccoSystem.login(userWrapper[0].getUserId(), userWrapper[0].getPassword());
                        balanceLabel.setText("Balance: " + saccoSystem.formatCurrency(userWrapper[0].getBalance()));
                    } else {
                        JOptionPane.showMessageDialog(frame, "Withdrawal failed! Insufficient balance or user not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid amount! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        transactionsButton.addActionListener(e -> {
            frame.dispose();
            showTransactionsScreen(userWrapper[0]);
        });

        logoutButton.addActionListener(e -> {
            frame.dispose();
            showLoginScreen();
        });
    }

    private void showTransactionsScreen(User user) {
        JFrame frame = new JFrame("SACCO Management System - Transactions");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel header = new JLabel("Transaction History", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setForeground(new Color(0, 102, 204));

        String[] columnNames = {"Date", "Type", "Amount"};
        List<Transaction> transactions = saccoSystem.getTransactions(user.getUserId(), 10);
        String[][] data = new String[transactions.size()][3];
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            data[i][0] = t.getTimestamp().toString();
            data[i][1] = t.getType();
            data[i][2] = saccoSystem.formatCurrency(t.getAmount());
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton backButton = new JButton("Back");
        JButton printButton = new JButton("Print Transactions");

        buttonPanel.add(backButton);
        buttonPanel.add(printButton);

        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);

        backButton.addActionListener(e -> {
            frame.dispose();
            showDashboard(user);
        });

        printButton.addActionListener(e -> {
            try {
                table.print(JTable.PrintMode.FIT_WIDTH,
                        new MessageFormat("Transaction History for " + user.getName()),
                        new MessageFormat("Page {0}"));
            } catch (java.awt.print.PrinterException ex) {
                JOptionPane.showMessageDialog(frame, "Printing failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SaccoGUI::new);
    }
}
