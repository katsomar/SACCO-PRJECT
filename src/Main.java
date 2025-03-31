import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SaccoSystem saccoSystem = new SaccoSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Signup");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.print("Enter User ID: ");
                String userId = scanner.nextLine();
                System.out.print("Enter Name: ");
                String name = scanner.nextLine();
                System.out.print("Enter Email: ");
                String email = scanner.nextLine();
                System.out.print("Enter Password: ");
                String password = scanner.nextLine();

                if (saccoSystem.signup(userId, name, email, password)) {
                    System.out.println("Signup successful!");
                } else {
                    System.out.println("User ID already exists.");
                }
            } else if (choice == 2) {
                System.out.print("Enter User ID: ");
                String userId = scanner.nextLine();
                System.out.print("Enter Password: ");
                String password = scanner.nextLine();

                User user = saccoSystem.login(userId, password);
                if (user != null) {
                    System.out.println("Login successful! Welcome, " + user.getName());
                    dashboard(scanner, saccoSystem, user); // Redirect to dashboard
                } else {
                    System.out.println("Invalid credentials.");
                }
            } else if (choice == 3) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }

        scanner.close();
    }

    private static void dashboard(Scanner scanner, SaccoSystem saccoSystem, User user) {
        while (true) {
            System.out.println("\n--- Dashboard ---");
            System.out.println("1. View Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. View Transactions");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                // Fetch the latest user data from the database
                user = saccoSystem.login(user.getUserId(), user.getPassword());
                if (user != null) {
                    System.out.println("Your current balance is: " + formatCurrency(user.getBalance()));
                } else {
                    System.out.println("Error fetching user data.");
                }
            } else if (choice == 2) {
                System.out.print("Enter amount to deposit: ");
                double amount = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                if (saccoSystem.deposit(user.getUserId(), amount)) {
                    System.out.println("Deposit successful! Amount: " + formatCurrency(amount));
                } else {
                    System.out.println("Deposit failed.");
                }
            } else if (choice == 3) {
                System.out.print("Enter amount to withdraw: ");
                double amount = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                if (saccoSystem.withdraw(user.getUserId(), amount)) {
                    System.out.println("Withdrawal successful! Amount: " + formatCurrency(amount));
                } else {
                    System.out.println("Insufficient balance.");
                }
            } else if (choice == 4) {
                saccoSystem.printStatement(user.getUserId());
            } else if (choice == 5) {
                System.out.println("Logging out...");
                break;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static String formatCurrency(double amount) {
        return "UGX " + String.format("%,.2f", amount);
    }
}
