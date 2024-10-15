package com.BankingApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String user = "root";
    private static final String password = "6204";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            Scanner scanner = new Scanner(System.in);

            User user = new User(conn, scanner);
            Accounts accounts = new Accounts(conn, scanner);
            AccountsManager accountsManager = new AccountsManager(conn, scanner);

            String email;
            long accountNumber;

            while (true) {
                System.out.println();
                System.out.println("Welcome to Banking Application");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if (email != null) {
                            System.out.println("User logged in.");
                            System.out.println();
                            if (!accounts.account_exists(email)) {
                                System.out.println("1. Open a new bank Account");
                                System.out.println("2. Exit");
                                int choice2 = scanner.nextInt();
                                if (choice2 == 1) {
                                    accountNumber = accounts.open_account(email);
                                    System.out.println("Account created successfully.");
                                    System.out.println("your account number is: " + accountNumber);
                                }
                                else {
                                    break;
                                }
                            }
                            accountNumber = accounts.getAccountNumber(email);
                            int choice2 = 0;
                            while (choice2 != 5) {
                                System.out.println("1. Withdraw Money");
                                System.out.println("2. Deposit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Exit");
                                choice2 = scanner.nextInt();

                                switch (choice2) {
                                    case 1:
                                        accountsManager.debit_money(accountNumber);
                                        break;
                                    case 2:
                                        accountsManager.credit_money(accountNumber);
                                        break;
                                    case 3:
                                        accountsManager.transfer_money(accountNumber);
                                        break;
                                    case 4:
                                        accountsManager.check_balance(accountNumber);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Invalid choice. Try again.");
                                        break;
                                }
                            }
                        }
                        else {
                            System.out.println("Invalid Email or Password. Try again.");
                        }
                    case 3:
                        System.out.println("Thank you for using Banking Application.");
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
