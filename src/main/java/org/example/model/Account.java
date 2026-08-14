package org.example.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Account {
    private Long accountId;
    private String accountNumber;
    private String accountFirstName;
    private String accountLastName;
    private String accountContactNumber;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Account(){}

    //for creating account
    public Account(String accountNumber, String accountFirstName, String accountLastName, String accountContactNumber, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.accountFirstName = accountFirstName;
        this.accountLastName = accountLastName;
        this.accountContactNumber = accountContactNumber;
        this.balance = balance;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountFirstName() {
        return accountFirstName;
    }

    public void setAccountFirstName(String accountFirstName) {
        this.accountFirstName = accountFirstName;
    }

    public String getAccountLastName() {
        return accountLastName;
    }

    public void setAccountLastName(String accountLastName) {
        this.accountLastName = accountLastName;
    }

    public String getAccountContactNumber() {
        return accountContactNumber;
    }

    public void setAccountContactNumber(String accountContactNumber) {
        this.accountContactNumber = accountContactNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
