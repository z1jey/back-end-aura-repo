package org.example.service;

import org.example.dao.AccountDao;
import org.example.model.Account;
import org.example.util.InputValidator;

import java.math.BigDecimal;
import java.sql.SQLException;
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


        //Asking for Firstname
        System.out.print("Enter First name: ");
        String firstName = scanner.nextLine();
        //Empty Validation
        if(!InputValidator.isNotEmpty(firstName)){
            System.out.println("[ERROR] Fist name cannot be empty!");
            return;
        }

        //Asking for Last Name
        System.out.print("Enter Last name: ");
        String lastName = scanner.nextLine();
        //last name empty validation
        if(!InputValidator.isNotEmpty(lastName)){
            System.out.println("[ERROR] Last name cannot be empty!");
            return;
        }

        //Asking for Contact Number
        System.out.print("Enter Contact Number: ");
        String contactNumber = scanner.nextLine();
        //contact number empty validation
        if(!InputValidator.isNotEmpty(contactNumber)) {
            System.out.println("[ERROR] Contact Number cannot be empty!");
            return;
        }
        //contact number checker validation
        if(!InputValidator.isContactNumberValid(contactNumber)){
            return;
        }

        //asking for initial amount
        System.out.print("Enter Initial Amount: ");
        String initialBalance = scanner.nextLine().trim();
        //empty initial amount validation
        if(!InputValidator.isNotEmpty(initialBalance)){
            System.out.println("[ERROR] Initial Amount cannot be empty!");
            return;
        }
        BigDecimal initialDeposit = new BigDecimal(initialBalance);
        //amount checker validation
        if(!InputValidator.isAmountValid(initialBalance)){
            return;
        }

        String accountNumber = generateAccountNumber();
        Account account = new Account(accountNumber, firstName, lastName, contactNumber, initialDeposit);

        try {
            accountDao.createAccount(account);
            System.out.println("\n[SUCCESS] Account Created Successfully!");
            printAccountSummary(account);
        } catch(SQLException exception) {
            System.out.println("Failed to create account for " + firstName);
            System.out.println("[ERROR] " + exception.getMessage());
        }

    }

    private String generateAccountNumber() {
        long seed = System.currentTimeMillis() % 1_000_000_000L;
        return String.format("ACC-%010d", seed);
    }

    private void printAccountSummary(Account account) {
        System.out.println("  Account Number : " + account.getAccountNumber());
        System.out.println("  Account Name   : " + account.getAccountFirstName() + " " + account.getAccountLastName());
        System.out.println("  Account Contact Number: " + account.getAccountContactNumber());
        System.out.printf("  Balance        : PHP %.2f%n", account.getBalance());
        if (account.getCreatedAt() != null) {
            System.out.println("  Created At     : " + account.getCreatedAt());
        }
    }
}
