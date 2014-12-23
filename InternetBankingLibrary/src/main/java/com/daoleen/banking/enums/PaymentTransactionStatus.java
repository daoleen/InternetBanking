package com.daoleen.banking.enums;

/**
 * Created by alex on 12/23/14.
 */
public enum PaymentTransactionStatus {
    OPENED(1), STEP2(2), STEP3(3), CLOSED(0);

    private final int status;

    private PaymentTransactionStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
