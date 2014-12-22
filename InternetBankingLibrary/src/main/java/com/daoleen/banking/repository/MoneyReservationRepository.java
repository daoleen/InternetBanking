package com.daoleen.banking.repository;

import com.daoleen.banking.domain.MoneyReservation;
import com.daoleen.banking.domain.PaymentTransaction;
import com.daoleen.banking.exception.NoEnoughMoneyException;

import java.util.List;

/**
 * Created by alex on 12/21/14.
 */
public interface MoneyReservationRepository extends Repository<MoneyReservation, Long> {
    public List<MoneyReservation> getActiveReservations(String cardNumber);
    public double getActiveReservationSum(String cardNumber);
    public MoneyReservation createReservation(String cardNumber, double amount, PaymentTransaction paymentTransaction) throws NoEnoughMoneyException;
    public void closeActiveReservation(Long id);
    public void closeActiveReservation(PaymentTransaction transaction);
}
