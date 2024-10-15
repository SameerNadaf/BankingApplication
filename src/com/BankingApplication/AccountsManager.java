package com.BankingApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountsManager {
    private Scanner scanner;
    private Connection connection;

    public AccountsManager(Connection conn, Scanner scanner) {
        this.connection = conn;
        this.scanner = scanner;
    }

    public void debit_money(long accountNumber) throws SQLException {
        System.out.println();
        System.out.print("Enter amount : ");
        double amount = scanner.nextDouble();
        System.out.print("Enter your security pin");
        String pin = scanner.next();

        try {
            connection.setAutoCommit(false);
            if (accountNumber!=0) {
                String query = "select * from accounts where account_number = ? and security_pin = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setLong(1, accountNumber);
                ps.setString(2, pin);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    double currentBalance = rs.getDouble("balance");
                    if (currentBalance >= amount) {
                        String query2 = "update accounts set balance = balance - ? where account_number = ?";
                        PreparedStatement ps2 = connection.prepareStatement(query2);
                        ps2.setDouble(1, amount);
                        ps2.setLong(2, accountNumber);
                        int rowsAffected = ps2.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Rs. " + amount + " Debited successfully");
                            System.out.println();
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }
                        else {
                            System.out.println("Transaction failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else {
                        System.out.println("Insufficient funds!");
                    }
                }
                else {
                    System.out.println("Invalid pin!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void credit_money(long account_number)throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if(account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? and security_pin = ? ");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1, amount);
                    preparedStatement1.setLong(2, account_number);
                    int rowsAffected = preparedStatement1.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Rs."+amount+" credited Successfully");
                        System.out.println();
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction Failed!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }else{
                    System.out.println("Invalid Security Pin!");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void transfer_money(long sender_account_number) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Receiver Account Number: ");
        long receiver_account_number = scanner.nextLong();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        try{
            connection.setAutoCommit(false);
            if(sender_account_number!=0 && receiver_account_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ? ");
                preparedStatement.setLong(1, sender_account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (amount<=current_balance){

                        // Write debit and credit queries
                        String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                        String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";

                        // Debit and Credit prepared Statements
                        PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);
                        PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);

                        // Set Values for debit and credit prepared statements
                        creditPreparedStatement.setDouble(1, amount);
                        creditPreparedStatement.setLong(2, receiver_account_number);
                        debitPreparedStatement.setDouble(1, amount);
                        debitPreparedStatement.setLong(2, sender_account_number);
                        int rowsAffected1 = debitPreparedStatement.executeUpdate();
                        int rowsAffected2 = creditPreparedStatement.executeUpdate();
                        if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                            System.out.println("Transaction Successful!");
                            System.out.println("Rs."+amount+" Transferred Successfully");
                            System.out.println();
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient Balance!");
                    }
                }else{
                    System.out.println("Invalid Security Pin!");
                }
            }else{
                System.out.println("Invalid account number");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void check_balance(long accountNumber) {
        scanner.nextLine();
        System.out.print("Enter your security pin : ");
        String pin = scanner.next();
        String sql = "select balance from accounts where account_number = ? and security_pin = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, accountNumber);
            ps.setString(2, pin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                System.out.println("Balance : " + balance);
                System.out.println();
            }
            else {
                System.out.println("Invalid pin!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
