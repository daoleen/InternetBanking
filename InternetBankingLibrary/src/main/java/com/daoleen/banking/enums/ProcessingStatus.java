package com.daoleen.banking.enums;

/**
 * Created by alex on 1/12/15.
 */
public enum ProcessingStatus {
    NOT_READY(0), SUCCESS(1), FAIL(2);

    private final int value;

    private ProcessingStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
