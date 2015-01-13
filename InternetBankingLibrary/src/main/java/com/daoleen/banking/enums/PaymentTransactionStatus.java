package com.daoleen.banking.enums;

/**
 * Created by alex on 12/23/14.
 */
public enum PaymentTransactionStatus {
    FILLING_DATA(1), PROCESSING(2), CLOSED(0);

    private final int status;

    private PaymentTransactionStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
