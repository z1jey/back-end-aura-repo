package org.example.service;

import org.example.dao.AccountDao;
import org.example.util.InputValidator;

import java.math.BigDecimal;
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
        String amount = scanner.nextLine().trim();
        //empty initial amount validation
        if(!InputValidator.isNotEmpty(amount)){
            System.out.println("[ERROR] Initial Amount cannot be empty!");
            return;
        }
        //amount checker validation
        if(!InputValidator.isAmountValid(amount)){
            return;
        }
    }
}
