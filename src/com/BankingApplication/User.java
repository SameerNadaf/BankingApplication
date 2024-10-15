package com.BankingApplication;

import java.sql.*;
import java.util.Scanner;

public class User {
    private Scanner scanner;
    private Connection connection;

    public User(Connection conn, Scanner scanner) {
        this.connection = conn;
        this.scanner = scanner;
    }

    public void register() {
        scanner.nextLine();
        System.out.print("Please enter your full name : ");
        String name = scanner.next();
        scanner.nextLine();
        System.out.print("Please enter your email address : ");
        String email = scanner.next();
        System.out.print("Please enter your password : ");
        String password = scanner.next();
        String sql = "INSERT INTO user(full_name, email, password) VALUES (?, ?, ?)";

        if (user_exists(email)) {
            System.out.println("User already exists for this email !");
            return;
        }

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User registered successfully.");
            }
            else {
                System.out.println("Registration failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String login() {
        scanner.nextLine();
        System.out.print("Please enter email : ");
        String email = scanner.next();
        System.out.print("Please enter password : ");
        String password = scanner.next();
        String sql = "SELECT * FROM user WHERE email = ? AND password = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return email;
            }
            else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean user_exists(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        try {
            CallableStatement call = connection.prepareCall(sql);
            call.setString(1, email);
            ResultSet rs = call.executeQuery();
            return rs.next();
        } catch (SQLException e ) {
            e.printStackTrace();
        }
        return false;
    }
}
