package org.example.service;
import org.example.dao.UserDao;
import org.example.model.User;
import java.sql.SQLException;
import java.util.Scanner;

public class LoginService {

    private final UserDao userDao;
    private final Scanner scanner;

    public LoginService(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void createAdminAccount() {
        System.out.println("\n========= CREATE ADMIN ACCOUNT =========");

        System.out.print("Enter admin name: ");
        String adminName = scanner.nextLine().trim();

        if (adminName.isEmpty()) {
            System.out.println("[ERROR] Admin name cannot be empty.");
            return;
        }

        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        if (username.isEmpty()) {
            System.out.println("[ERROR] Username cannot be empty.");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        if (password.isEmpty()) {
            System.out.println("[ERROR] Password cannot be empty.");
            return;
        }

        try {
            User existingUser = userDao.findByUsername(username);
            if (existingUser != null) {
                System.out.println("[ERROR] Username already exists.");
                return;
            }

            User user = new User(adminName, username, password);

            userDao.createUser(user);
            System.out.println("[SUCCESS] Admin account created successfully!");

        } catch (SQLException exception) {
            System.out.println("[ERROR] Failed to create admin account.");
            System.out.println("[ERROR] " + exception.getMessage());
        }
    }

    public boolean login() {

        System.out.println("\n========= LOGIN =========");

        System.out.print("Enter admin Username: ");
        String adminUsername = scanner.nextLine().trim();

        if (adminUsername.isEmpty()) {
            System.out.println("[ERROR] Username cannot be empty.");
            return false;
        }

        System.out.print("Enter admin Password: ");
        String adminPassword = scanner.nextLine().trim();

        if (adminPassword.isEmpty()) {
            System.out.println("[ERROR] Password cannot be empty.");
            return false;
        }

        try {
            User user = userDao.findByUsername(adminUsername);
            if (user == null) {
                System.out.println("[ERROR] No account found.");
                return false;
            }

            if (!adminPassword.equals(user.getPassword())) {
                System.out.println("[ERROR] Wrong password.");
                return false;
            }

            System.out.println("[SUCCESS] Login successful!");
            System.out.println("\nWelcome, " + user.getAdminName() + "!");

            return true;

        } catch (SQLException exception) {
            System.out.println("[ERROR] Login failed.");
            System.out.println("[ERROR] " + exception.getMessage());
            return false;
        }
    }
}