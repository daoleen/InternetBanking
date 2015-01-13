package com.daoleen.banking.repository;

import com.daoleen.banking.domain.PaymentTransaction;
import com.daoleen.banking.exception.NoEnoughMoneyException;
import com.daoleen.banking.exception.PaymentTransactionException;

import java.util.List;
import java.util.UUID;

/**
 * Created by alex on 12/17/14.
 */
public interface PaymentTransactionRepository extends Repository<PaymentTransaction, UUID> {
    public List<PaymentTransaction> findByCardNumber(String cardNumber);
    public void createTransaction(int recipientBankId, String senderCardNumber, double amount) throws PaymentTransactionException, NoEnoughMoneyException;
    public void fillDataForCardTransfer(String recipientCardNumber);
    public void fillDataForMoneyTransfer(String rcpAccountNumber, String rcpFirstName, String rcpLastName, String rcpPatronymicName);
    public void completeProcessing() throws PaymentTransactionException;
    public PaymentTransaction getPaymentTransaction();
}
