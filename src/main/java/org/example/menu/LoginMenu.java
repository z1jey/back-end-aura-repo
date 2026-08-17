package org.example.menu;

import org.example.dao.UserDao;
import org.example.dao.impl.UserDaoImplementation;
import org.example.service.LoginService;

import java.util.Scanner;

public class LoginMenu {

    private final Scanner scanner;
    private final LoginService loginService;

    public LoginMenu() {
        scanner = new Scanner(System.in);
        UserDao userDao = new UserDaoImplementation();
        loginService = new LoginService(userDao, scanner);
    }

    public void startLoginMenu() {
        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    loginService.createAdminAccount();
                    break;
                case "2":
                    if (loginService.login()) {
                        BankingMenu bankingMenu = new BankingMenu(scanner);
                        bankingMenu.startBankingMenu();
                    }
                    break;
                case "3":
                    running = false;
                    System.out.println("\nThank you for using the Banking Management System. Goodbye!");
                    break;

                default:
                    System.out.println( "[ERROR] Invalid choice. Please select 1-3.");
            }
        }
        scanner.close();
    }

    private void printMenu() {

        System.out.println();
        System.out.println("==================================");
        System.out.println("      BANKING MANAGEMENT SYSTEM   ");
        System.out.println("==================================");
        System.out.println(" 1. Create Admin Account");
        System.out.println(" 2. Login");
        System.out.println(" 3. Exit");
        System.out.println("==================================");
    }
}