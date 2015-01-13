package com.daoleen.banking.exception;

/**
 * Created by alex on 1/12/15.
 */
public class PaymentTransactionException extends Exception {
    private static final long serialVersionUID = -2734654360929415896L;

    public PaymentTransactionException(String message) {
        super(message);
    }
}
