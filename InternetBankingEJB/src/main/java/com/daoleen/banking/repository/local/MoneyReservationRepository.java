package com.daoleen.banking.repository.local;

import com.daoleen.banking.domain.MoneyReservation;
import com.daoleen.banking.domain.PaymentCard;
import com.daoleen.banking.domain.PaymentTransaction;
import com.daoleen.banking.exception.NoEnoughMoneyException;
import com.daoleen.banking.repository.Repository;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by alex on 1/17/15.
 */
@Local
public interface MoneyReservationRepository extends Repository<MoneyReservation, Long> {
    public List<MoneyReservation> getActiveReservations(String cardNumber);
    public double getActiveReservationSum(PaymentCard card);
    public MoneyReservation createReservation(PaymentCard card, double amount, PaymentTransaction paymentTransaction) throws NoEnoughMoneyException;
    public void closeActiveReservation(Long id);
    public void closeActiveReservation(PaymentTransaction transaction);
}
