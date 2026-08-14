package org.example.service;

import org.example.dao.AccountDao;
import org.example.util.InputValidator;

import java.util.Scanner;

public class AccountService {

    private final AccountDao accountDao;
    private final Scanner scanner;

    public AccountService(AccountDao accountDao, Scanner scanner) {
        this.accountDao = accountDao;
        this.scanner = scanner;
    }

    public void createAccountService(){
        System.out.println("\n========= CREATE ACCOUNT =========");

        System.out.print("Enter First name: ");
        String firstName = scanner.nextLine().trim();
        if(!InputValidator.isNotEmpty(firstName)){
            System.out.println("[ERROR] Fist name cannot be empty!");
            return;
        }

        System.out.print("Enter Last name: ");
        String lastName = scanner.nextLine().trim();
        if(!InputValidator.isNotEmpty(lastName)){
            System.out.println("[ERROR] Last name cannot be empty!");
            return;
        }

        System.out.println("Enter Contact Number");
        String contactNumber = scanner.nextLine();

    }
}
