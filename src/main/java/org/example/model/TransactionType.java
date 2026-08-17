package org.example.model;

public enum TransactionType {
    WITHDRAW, DEPOSIT, TRANSFER_IN, TRANSFER_OUT;
    public String displayName() {
        return name().replace('_', ' ');
    }
}
