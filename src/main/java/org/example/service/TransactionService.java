package org.example.service;
import org.example.config.DbConnection;
import org.example.dao.AccountDao;
import org.example.dao.TransactionDao;
import org.example.exception.AccountNotFoundException;
import org.example.exception.InsufficientBalanceException;
import org.example.exception.InvalidTransactionException;
import org.example.model.Account;
import org.example.model.Transaction;
import org.example.model.TransactionType;
import org.example.util.InputValidator;
import org.example.util.ReferenceNumberGenerator;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;


public class TransactionService {

    private static final int MINI_STATEMENT = 5;
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final AccountDao accountDao;
    private final TransactionDao  transactionDao;
    private final Scanner input;
    private final AccountService accountService;

    public TransactionService(AccountDao accountDao, TransactionDao transactionDao, Scanner input, AccountService accountService) {
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
        this.input = input;
        this.accountService = accountService;
    }

    public void withdraw(){
        System.out.println("\n========= WITHDRAWAL =========");

        try {
            System.out.print("Enter Account number: ");
            String accountNumber = input.nextLine();
            if(!InputValidator.isNotEmpty(accountNumber)) {
                System.out.println("[ERROR]  Account number cannot be empty.");
                return;
            }
            //check if it exist
            Account account = accountService.findAccountOrThrow(accountNumber);
            System.out.print("Enter withdrawal Amount: ");
            String withdrawAmount = input.nextLine();
            if(!InputValidator.isNotEmpty(withdrawAmount)){
                System.out.println("[ERROR] Withdrawal amount cannot be empty.");
                return;
            }
            if(!InputValidator.isPositiveAmount(withdrawAmount)) {
                return;
            }
            //convert
            BigDecimal withdrawal = new BigDecimal(withdrawAmount);

            validateSufficientBalance(account, withdrawal);
            BigDecimal newBalance = account.getBalance().subtract(withdrawal);
            accountDao.updateBalance(account.getAccountNumber(), newBalance);

            Transaction transaction = new Transaction(account.getAccountNumber(), TransactionType.WITHDRAW,
                    withdrawal, newBalance, ReferenceNumberGenerator.generate(),
                                                        "Cash  Withdrawal");
            transactionDao.save(transaction);
            System.out.println("[SUCCESS] Withdrawal Successful!");
            printTransactionReceipt(transaction, account.getAccountFirstName() + " "+ account.getAccountLastName());
        } catch (AccountNotFoundException |
                 InsufficientBalanceException exception) {
            System.out.println("[ERROR]  " + exception.getMessage());
        } catch (SQLException sqlException) {
            System.out.println("[ERROR] " +  sqlException);
        }
    }

    public void deposit(){
        System.out.println("\n========= DEPOSIT =========");

        try {
            System.out.print("Enter Account Number: ");
            String accountNumber = input.nextLine();
            if(!InputValidator.isNotEmpty(accountNumber)){
                System.out.println("[ERROR] Account Number Cannot be empty.");
                return;
            }

            Account account = accountService.findAccountOrThrow(accountNumber);
            System.out.print("Enter amount to deposit: ");
            String amountDeposit = input.nextLine();
            if(!InputValidator.isPositiveAmount(amountDeposit)) {
                return;
            }

            BigDecimal deposit = new BigDecimal(amountDeposit);
            BigDecimal newBalance = account.getBalance().add(deposit);
            accountDao.updateBalance(account.getAccountNumber(), newBalance);

            Transaction transaction = new Transaction(account.getAccountNumber(), TransactionType.DEPOSIT,
                    deposit, newBalance, ReferenceNumberGenerator.generate(), "Cash Deposit");

            transactionDao.save(transaction);
            System.out.println("[SUCCESS] Deposit Successful!");
            printTransactionReceipt(transaction, account.getAccountFirstName() + " "+ account.getAccountLastName());
        } catch (AccountNotFoundException exception) {
            System.out.println("[ERROR]  " + exception.getMessage());
        } catch (SQLException sqlException) {
            System.out.println("[ERROR] " +  sqlException);
        }
    }

    public void transfer(){
        System.out.println("\n========= FUND TRANSFER =========");
        Account receiver;
        Account sender;
        BigDecimal transfer;
        String receiverAccount;
        String senderAccount;

        try {
            System.out.print("Enter sender Account: ");
            senderAccount = input.nextLine().trim();
            if(!InputValidator.isNotEmpty(senderAccount)){
                System.out.println("[ERROR] Sender account cannot be empty");
                return;
            }
            System.out.print("Enter receiver Account: ");
            receiverAccount = input.nextLine().trim();

            if(!InputValidator.isNotEmpty(receiverAccount)){
                System.out.println("[ERROR] Receiver account cannot be empty");
                return;
            }

            if (senderAccount.equalsIgnoreCase(receiverAccount)) {
                throw new InvalidTransactionException("Sender and receiver cannot be the same account.");
            }

            sender = accountService.findAccountOrThrow(senderAccount);
            receiver = accountService.findAccountOrThrow(receiverAccount);

            System.out.print("Enter transfer amount: ");
            String transferAmount = input.nextLine();
            if(!InputValidator.isNotEmpty(transferAmount)){
                System.out.println("[ERROR] Transfer Amount cannot be empty.");
                return;
            }
            if(!InputValidator.isPositiveAmount(transferAmount)){
                return;
            }

            transfer = new BigDecimal(transferAmount);
            validateSufficientBalance(sender, transfer);

        } catch (AccountNotFoundException | InvalidTransactionException | InsufficientBalanceException exception) {
            System.out.println("[ERROR]  " + exception.getMessage());
            return;
        } catch (SQLException sqlException) {
            System.out.println("[ERROR] " +  sqlException);
            return;
        }

        BigDecimal newSenderBalance = sender.getBalance().subtract(transfer);
        BigDecimal newReceiverBalance = receiver.getBalance().add(transfer);

        String sharedRef = ReferenceNumberGenerator.generate();

        Transaction senderTransaction = new Transaction(senderAccount, TransactionType.TRANSFER_OUT,
                                                    transfer, newSenderBalance, sharedRef,
                "Transfer to " + receiverAccount + " (" + receiver.getAccountFirstName() + " " + receiver.getAccountLastName() + ") " );
        Transaction receiverTransaction = new Transaction(receiverAccount, TransactionType.TRANSFER_IN, transfer,
                                                            newReceiverBalance, ReferenceNumberGenerator.generate(),
                "Transfer from " + senderAccount + " (" + sender.getAccountFirstName() + " " + sender.getAccountLastName() + ") " );

        try (Connection connection = DbConnection.getConnection()) {
                connection.setAutoCommit(false);

            try {
                accountDao.updateBalance(connection, senderAccount, newSenderBalance);
                accountDao.updateBalance(connection,  receiverAccount, newReceiverBalance);
                transactionDao.save(connection,senderTransaction);
                transactionDao.save(connection, receiverTransaction);
                connection.commit();

                System.out.println("\n========= SENDER =========");
                printTransactionReceipt(senderTransaction,sender.getAccountFirstName() + " " + sender.getAccountLastName());

                System.out.println("\n========= RECEIVER =========");
                printTransactionReceipt(receiverTransaction, receiver.getAccountFirstName() + " " + receiver.getAccountLastName());

            } catch (SQLException exception) {
                try {
                    connection.rollback();
                    System.out.println("[WARNING] Transfer rolled back for ref: "  +  sharedRef);
                } catch (SQLException sqlException) {
                    System.out.println("[ERROR] Rollback Failed! " + sqlException);
                }
                System.out.println("[ERROR] Transfer failed.");
                System.out.println("[ERROR] " + exception.getMessage());
            }  finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ignored) {

                }
            }

        } catch (SQLException exception) {
            System.out.println("[ERROR] Could not connect to the database.");
            System.out.println("[ERROR] " + exception.getMessage());
        }
    }

    public void viewTransaction(){
        System.out.println("\n========= TRANSACTION HISTORY =========");

        try {
            System.out.print("Enter Account Number: ");
            String accountNumber = input.nextLine();
            if(!InputValidator.isNotEmpty(accountNumber)){
                System.out.println("[ERROR] Account Number Cannot be empty.");
                return;
            }

            Account account = accountService.findAccountOrThrow(accountNumber);
            List<Transaction> transactions = transactionDao.findByAccountTransaction(accountNumber);
            System.out.println("\n  Account : " + account.getAccountNumber());
            System.out.println("  Name    : " + account.getAccountFirstName() + " " +account.getAccountLastName());
            System.out.printf("  Balance : PHP %.2f%n%n", account.getBalance());

            if(transactions.isEmpty()) {
                System.out.println("No transaction found on this account: " + accountNumber);
                return;
            }

            System.out.printf("%nTransaction History — %s%n", accountNumber);
            printTransactionsTable(transactions);

        } catch (AccountNotFoundException exception) {
            System.out.println("[ERROR]  " + exception.getMessage());
        } catch (SQLException sqlException) {
            System.out.println("[ERROR] Failed to fetch Transaction History ");
            System.out.println("[ERROR] " +  sqlException.getMessage());
        }
    }

    public void miniStatement(){
        System.out.println("\n========= MINI STATEMENT (Last " + MINI_STATEMENT + " Transactions) =========");
        try {
            System.out.print("Enter Account Number: ");
            String accountNumber = input.nextLine();
            if(!InputValidator.isNotEmpty(accountNumber)){
                System.out.println("[ERROR] Account Number Cannot be empty.");
                return;
            }

            Account account = accountService.findAccountOrThrow(accountNumber);
            List<Transaction> transactions = transactionDao.findByRecentTransaction(accountNumber, MINI_STATEMENT);

            System.out.println("\n  Account : " + account.getAccountNumber());
            System.out.println("  Name    : " + account.getAccountFirstName() + " " +account.getAccountLastName());
            System.out.printf("  Balance : PHP %.2f%n%n", account.getBalance());

            if(transactions.isEmpty()) {
                System.out.println("No transaction found on this account: " + accountNumber);
                return;
            }

            System.out.printf("%nLast 5 Transaction History — %s%n", accountNumber);
            printTransactionsTable(transactions);

        } catch (AccountNotFoundException exception) {
            System.out.println("[ERROR]  " + exception.getMessage());
        } catch (SQLException sqlException) {
            System.out.println("[ERROR] Failed to fetch mini statement ");
            System.out.println("[ERROR] " +  sqlException.getMessage());
        }
    }


    private void validateSufficientBalance(Account account, BigDecimal amount)
            throws InsufficientBalanceException {

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(account.getBalance(), amount);
        }
    }

    private void printTransactionReceipt(Transaction transaction, String accountName) {
        System.out.println("  ----------------------------------");
        System.out.println("  Account       : " + transaction.getAccountNumber());
        System.out.println("  Account Name  : " + accountName);
        System.out.println("  Type          : " + transaction.getTransactionType().displayName());
        System.out.printf("  Amount        : PHP %.2f%n", transaction.getAmount());
        System.out.printf("  Balance After : PHP %.2f%n", transaction.getBalanceAfter());
        System.out.println("  Reference     : " + transaction.getReferenceNumber());
        if (transaction.getRemarks() != null && !transaction.getRemarks().isEmpty()) {
            System.out.println("  Remarks       : " + transaction.getRemarks());
        }
        System.out.println("  ----------------------------------");
    }

    private void printTransactionsTable(List<Transaction> transactions) {
        String line = "+----+---------------------+--------------+--------------+--------------+---------------------+";
        System.out.println(line);
        System.out.printf("| %-2s | %-19s | %-12s | %-12s | %-12s | %-19s |%n",
                "No", "Reference", "Type", "Amount", "Balance Aftr", "Date/Time");
        System.out.println(line);

        int i = 1;
        for (Transaction t : transactions) {
            System.out.printf("| %-2d | %-19s | %-12s | %12.2f | %12.2f | %-19s |%n",
                    i++,
                    t.getReferenceNumber().length() > 19
                            ? t.getReferenceNumber().substring(0, 19) : t.getReferenceNumber(),
                    t.getTransactionType().displayName(),
                    t.getAmount(),
                    t.getBalanceAfter(),
                    t.getCreated_at() != null ? t.getCreated_at().format(DISPLAY_FORMAT) : "—"
            );
        }
        System.out.println(line);
    }
}
