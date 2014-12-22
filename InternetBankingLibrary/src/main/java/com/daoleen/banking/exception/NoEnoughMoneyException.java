package com.daoleen.banking.exception;

/**
 * Created by alex on 12/21/14.
 */
public class NoEnoughMoneyException extends Exception {
    private static final long serialVersionUID = -8869314677300075276L;

    public NoEnoughMoneyException(String cardNumber, double amount) {
        super(String.format("The card '%s' does not have '%.2f' money", cardNumber, amount));
    }
}
