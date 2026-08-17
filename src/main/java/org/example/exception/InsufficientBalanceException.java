package org.example.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends Exception {

    private final BigDecimal availableBalance;
    private final BigDecimal requestedAmount;

    public InsufficientBalanceException(BigDecimal availableBalance, BigDecimal requestedAmount) {
        super(String.format(
                "Insufficient balance. Available: %.2f, Requested: %.2f",
                availableBalance, requestedAmount
        ));
        this.availableBalance = availableBalance;
        this.requestedAmount  = requestedAmount;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }
}