package org.example.menu;

import org.example.dao.AccountDao;
import org.example.dao.TransactionDao;
import org.example.dao.impl.AccountDaoImplementation;
import org.example.dao.impl.TransactionDaoImplementation;
import org.example.service.AccountService;
import org.example.service.TransactionService;

import java.util.Scanner;

public class BankingMenu {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final Scanner scanner;

    public BankingMenu(Scanner scanner) {
        this.scanner = scanner;
        AccountDao accountDao = new AccountDaoImplementation();
        TransactionDao transactionDao = new TransactionDaoImplementation();
        this.accountService = new AccountService(accountDao, scanner );
        this.transactionService = new TransactionService(accountDao, transactionDao, scanner, accountService);
    }

    public void startBankingMenu() {
        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    accountService.createAccountService();
                    break;
                case "2":
                    accountService.balanceInquiry();
                    break;
                case "3":
                    transactionService.deposit();
                    break;
                case "4":
                    transactionService.withdraw();
                    break;
                case "5":
                    transactionService.transfer();
                    break;
                case "6":
                    transactionService.viewTransaction();
                    break;
                case "7":
                    transactionService.miniStatement();
                    break;
                case "8":
                    accountService.listAccounts();
                    break;
                case "9":
                    accountService.searchAccountService();
                    break;
                case "10":
                    accountService.deleteAccountService();
                    break;
                case "11":
                    accountService.viewArchivedAccounts();
                    break;
                case "12":
                    running = false;
                    System.out.println("\nThank you for using the Banking Management System. Goodbye!");
                    break;
                default:
                    System.out.println("[ERROR] Invalid choice. Please select from 1-12.");
            }
        }
    }

    private void printMenu() {

        System.out.println();
        System.out.println("==================================");
        System.out.println("      BANKING MANAGEMENT SYSTEM   ");
        System.out.println("==================================");
        System.out.println(" 1. Create Account");
        System.out.println(" 2. Balance Inquiry");
        System.out.println(" 3. Deposit");
        System.out.println(" 4. Withdraw");
        System.out.println(" 5. Transfer");
        System.out.println(" 6. Transaction History");
        System.out.println(" 7. Mini Statement");
        System.out.println(" 8. List All Accounts");
        System.out.println(" 9. Search Account");
        System.out.println("10. Delete Account");
        System.out.println("11. Archived Accounts");
        System.out.println("12. Exit");
        System.out.println("==================================");
    }
}