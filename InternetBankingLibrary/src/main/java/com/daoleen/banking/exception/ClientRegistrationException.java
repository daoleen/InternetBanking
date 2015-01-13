package com.daoleen.banking.exception;

/**
 * Created by alex on 1/12/15.
 */
public class ClientRegistrationException extends Exception {
    private static final long serialVersionUID = 3928533922912286630L;

    public ClientRegistrationException(String message) {
        super(message);
    }
}
