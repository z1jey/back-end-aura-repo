package org.example.service;

import org.example.dao.AccountDao;
import org.example.model.Account;
import org.example.util.InputValidator;
import org.example.exception.AccountNotFoundException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
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
        if(!InputValidator.isAmountValid(initialBalance)){
            return;
        }
        BigDecimal initialDeposit = new BigDecimal(initialBalance);
        //amount checker validation
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

    public void balanceInquiry(){
        System.out.println("\n========= BALANCE INQUIRY =========");

        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine().trim();

        try{
            Account account = findAccountOrThrow(accountNumber);
            System.out.println("\n[SUCCESS] Balance Inquiry");
            printAccountSummary(account);
        } catch (AccountNotFoundException exception) {
            System.out.println("[ERROR] " + exception.getMessage());
        } catch (SQLException exception) {
            System.out.println("[ERROR] Balance Inquiry failed for: " + accountNumber);
            System.out.println("[ERROR] Database Error " + exception.getMessage());
        }
    }

    public void listAccounts(){
        System.out.println("\n========= LIST OF ALL ACCOUNTS =========");
        try {
            List<Account> accountList = accountDao.findAllAccounts();

            if(!InputValidator.isListNotEmpty(accountList)){
                System.out.println("[ERROR] No accounts found.");
                return;
            }
            printAccountsTable(accountList);
        } catch (SQLException exception) {
            System.out.println("[ERROR] Failed to retrieve List accounts");
            System.out.println("[ERROR] " + exception.getMessage());
        }
    }

    public void deleteAccountService() {
        System.out.println("\n========= DELETE ACCOUNT =========");

        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine().trim();

        if (!InputValidator.isNotEmpty(accountNumber)) {
            System.out.println("[ERROR] Account Number cannot be empty.");
            return;
        }

        try {
            Account account = findAccountOrThrow(accountNumber);
            System.out.println("\nAccount Information");
            System.out.println("Account Number : " + account.getAccountNumber());
            System.out.println("Account Name   : "  + account.getAccountFirstName() + " " + account.getAccountLastName());
            System.out.println("Contact Number : " + account.getAccountContactNumber());
            System.out.printf("Balance        : PHP %.2f%n", account.getBalance());

            if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
                System.out.println("[ERROR] Account cannot be deleted while it has a remaining balance." );
                System.out.printf("[INFO] Remaining Balance: PHP %.2f%n", account.getBalance());
                return;
            }

            System.out.print("\nAre you sure you want to delete account : " + accountNumber + " [Y / N]: ");
            String confirmation = scanner.nextLine().trim();

            if (!InputValidator.isNotEmpty(confirmation)) {
                System.out.println("[ERROR] Confirmation cannot be empty.");
                return;
            }

            if (!confirmation.equalsIgnoreCase("Y")) {
                System.out.println("[INFO] Account deletion cancelled.");
                return;
            }

            // Delete account
            accountDao.deleteAccount(accountNumber);
            System.out.println("[SUCCESS] Account deleted successfully!");

        } catch (AccountNotFoundException exception) {
            System.out.println("[ERROR] " + exception.getMessage());
        } catch (SQLException sqlException) {
            System.out.println("[ERROR] Failed to delete account.");
            System.out.println("[ERROR] " + sqlException.getMessage());
        }
    }

    public void searchAccountService() {
        System.out.println("\n========= SEARCH ACCOUNT =========");

        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine().trim();

        if (!InputValidator.isNotEmpty(accountNumber)) {
            System.out.println("[ERROR] Account Number cannot be empty.");
            return;
        }

        try {
            Account account = findAccountOrThrow(accountNumber);
            System.out.println("\n========= ACCOUNT FOUND =========");
            System.out.println("Account Number : " + account.getAccountNumber());
            System.out.println("First Name     : " + account.getAccountFirstName());
            System.out.println("Last Name      : " + account.getAccountLastName());
            System.out.println("Contact Number : " + account.getAccountContactNumber());
            System.out.printf("Balance        : PHP %.2f%n", account.getBalance());

            if (account.getCreatedAt() != null) {
                System.out.println("Created At     : " + account.getCreatedAt());
            } else {
                System.out.println("Created At     : —");
            }

        } catch (AccountNotFoundException exception) {
            System.out.println("[ERROR] " + exception.getMessage());
        } catch (SQLException sqlException) {
            System.out.println("[ERROR] Failed to search account.");
            System.out.println("[ERROR] " + sqlException.getMessage());
        }
    }

    //helpers
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

    public Account findAccountOrThrow(String accountNumber) throws AccountNotFoundException, SQLException {
        Optional<Account> opt = accountDao.findByAccountNumber(accountNumber);
        return opt.orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    private void printAccountsTable(List<Account> accounts) {
        String line = "+----+-----------------------+-------------------------+---------------+---------------+---------------------+";

        System.out.println(line);
        System.out.printf("| %-2s | %-21s | %-23s | %-13s | %-13s | %-19s |%n",
                "No", "Account Number", "Account Name", "Contact Number", "Balance (PHP)", "Created At");
        System.out.println(line);

        int i = 1;

        for (Account a : accounts) {
            String accountName = a.getAccountFirstName() + " " + a.getAccountLastName();
            System.out.printf("| %-2d | %-21s | %-23s | %-13s | %13.2f | %-19s |%n",
                    i++,
                    a.getAccountNumber(),
                    accountName,
                    a.getAccountContactNumber(),
                    a.getBalance(),
                    a.getCreatedAt() != null
                            ? a.getCreatedAt().toString().replace("T", " ")
                            : "—"
            );
        }

        System.out.println(line);
        System.out.println("  Total accounts: " + accounts.size());
    }
}
