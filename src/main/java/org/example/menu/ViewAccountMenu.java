package org.example.menu;
import org.example.service.AccountService;
import java.util.Scanner;

public class ViewAccountMenu {

    private final AccountService accountService;
    private final Scanner scanner;

    public ViewAccountMenu(AccountService accountService, Scanner scanner) {
        this.accountService = accountService;
        this.scanner = scanner;
    }

    public void startViewAccountMenu() {
        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    accountService.listAccounts();
                    break;
                case "2":
                    accountService.listActiveAccounts();
                    break;
                case "3":
                    accountService.viewArchivedAccounts();
                    break;
                case "4":
                    running = false;
                    break;
                default:
                    System.out.println("[ERROR] Invalid choice. Please select from 1-4.");
            }
        }
    }

    private void printMenu() {

        System.out.println();
        System.out.println("==================================");
        System.out.println("          VIEW ACCOUNTS           ");
        System.out.println("==================================");
        System.out.println(" 1. List All Accounts");
        System.out.println(" 2. List All Active Accounts");
        System.out.println(" 3. Archived Accounts");
        System.out.println(" 4. Back");
        System.out.println("==================================");
    }
}