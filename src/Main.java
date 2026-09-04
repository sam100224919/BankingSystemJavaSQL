import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Banking Management System ===");
            System.out.println("1. Create Account");
            System.out.println("2. Check Balance");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Exit");
            System.out.print("Select choice: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Enter a number.");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    AccountRepository.createAccount(scanner);
                    break;
                case 2:
                    AccountRepository.checkBalance(scanner);
                    break;
                case 3:
                    AccountRepository.deposit(scanner);
                    break;
                case 4:
                    AccountRepository.withdraw(scanner);
                    break;
                case 5:
                    System.out.println("Exiting application.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (choice != 5);

        scanner.close();
    }
}
