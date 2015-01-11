package com.daoleen.banking.exception;

/**
 * Created by alex on 1/11/15.
 */
public class UserRegistrationException extends Exception {
    private static final long serialVersionUID = -2811638591989136625L;

    public UserRegistrationException(String message) {
        super(message);
    }
}
