package com.daoleen.banking.enums;

/**
 * Created by alex on 12/22/14.
 */
public enum MoneyReservationStatus {
    OPENED(1), CLOSED(2);

    private final int value;

    private MoneyReservationStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
