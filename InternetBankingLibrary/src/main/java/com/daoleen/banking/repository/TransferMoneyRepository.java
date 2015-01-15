package com.daoleen.banking.repository;

import com.daoleen.banking.domain.PaymentTransaction;
import com.daoleen.banking.exception.NoEnoughMoneyException;
import com.daoleen.banking.exception.PaymentTransactionException;

/**
 * Created by alex on 1/14/15.
 */
public interface TransferMoneyRepository {
    public void createTransaction(int recipientBankId, String senderCardNumber, double amount) throws PaymentTransactionException, NoEnoughMoneyException;
    public void fillDataForCardTransfer(String recipientCardNumber);
    public void fillDataForMoneyTransfer(String rcpAccountNumber, String rcpFirstName, String rcpLastName, String rcpPatronymicName);
    public void completeProcessing() throws PaymentTransactionException;
    public PaymentTransaction getPaymentTransaction();
}
