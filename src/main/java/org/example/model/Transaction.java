package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private Long transactionId;
    private String accountNumber;
    private TransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String referenceNumber;
    private String remarks;
    private LocalDateTime created_at;

    public Transaction () {}

    public Transaction(String accountNumber, TransactionType transactionType, BigDecimal amount, BigDecimal balanceAfter, String referenceNumber, String remarks) {
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.referenceNumber = referenceNumber;
        this.remarks = remarks;
    }


}
