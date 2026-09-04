import java.sql.*;
import java.util.Scanner;

public class AccountRepository {

    public static void createAccount(Scanner scanner) {
        System.out.print("Enter Holder Name: ");
        String name = scanner.nextLine();
        System.out.print("Set 4-digit PIN: ");
        String pin = scanner.nextLine();
        System.out.print("Initial Deposit Amount: ");
        double balance = scanner.nextDouble();
        scanner.nextLine();

        String sql = "INSERT INTO accounts (holder_name, balance, pin) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, balance);
            pstmt.setString(3, pin);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int accountId = rs.getInt(1);
                System.out.println("Account created successfully! Account ID: " + accountId);

                String txSql = "INSERT INTO transactions (account_id, transaction_type, amount) VALUES (?, 'DEPOSIT', ?)";
                try (PreparedStatement txStmt = conn.prepareStatement(txSql)) {
                    txStmt.setInt(1, accountId);
                    txStmt.setDouble(2, balance);
                    txStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void checkBalance(Scanner scanner) {
        System.out.print("Enter Account ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        String sql = "SELECT balance FROM accounts WHERE account_id = ? AND pin = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, pin);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Current Balance: $" + rs.getDouble("balance"));
            } else {
                System.out.println("Invalid Account ID or PIN.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deposit(Scanner scanner) {
        System.out.print("Enter Account ID: ");
        int id = scanner.nextInt();
        System.out.print("Enter Deposit Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        String sql = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Successfully deposited $" + amount);

                String txSql = "INSERT INTO transactions (account_id, transaction_type, amount) VALUES (?, 'DEPOSIT', ?)";
                try (PreparedStatement txStmt = conn.prepareStatement(txSql)) {
                    txStmt.setInt(1, id);
                    txStmt.setDouble(2, amount);
                    txStmt.executeUpdate();
                }
            } else {
                System.out.println("Account ID not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void withdraw(Scanner scanner) {
        System.out.print("Enter Account ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();
        System.out.print("Enter Withdrawal Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        String checkSql = "SELECT balance FROM accounts WHERE account_id = ? AND pin = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, id);
            checkStmt.setString(2, pin);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");
                if (currentBalance >= amount) {
                    String updateSql = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setDouble(1, amount);
                        updateStmt.setInt(2, id);
                        updateStmt.executeUpdate();
                        System.out.println("Successfully withdrew $" + amount);

                        String txSql = "INSERT INTO transactions (account_id, transaction_type, amount) VALUES (?, 'WITHDRAWAL', ?)";
                        try (PreparedStatement txStmt = conn.prepareStatement(txSql)) {
                            txStmt.setInt(1, id);
                            txStmt.setDouble(2, amount);
                            txStmt.executeUpdate();
                        }
                    }
                } else {
                    System.out.println("Error: Insufficient funds.");
                }
            } else {
                System.out.println("Invalid Account ID or PIN.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}