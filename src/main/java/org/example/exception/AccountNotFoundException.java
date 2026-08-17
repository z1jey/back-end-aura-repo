package org.example.exception;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String accountNumber) {
    super("Account not found: " + accountNumber);
}

    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
