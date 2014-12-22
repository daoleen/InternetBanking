package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.MoneyReservation;
import com.daoleen.banking.domain.PaymentCard;
import com.daoleen.banking.domain.PaymentTransaction;
import com.daoleen.banking.exception.NoEnoughMoneyException;
import com.daoleen.banking.repository.MoneyReservationRepository;
import com.daoleen.banking.repository.PaymentCardRepository;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.inject.Typed;
import java.util.List;

/**
 * Created by alex on 12/21/14.
 */
@Stateless
@Local(MoneyReservationRepository.class)
@Typed(MoneyReservationRepository.class)
public class MoneyReservationBean extends AbstractBean<MoneyReservation, Long>
        implements MoneyReservationRepository
{

    @EJB
    private PaymentCardRepository paymentCardRepository;

    public MoneyReservationBean() {
        super(MoneyReservation.class);
    }

    @Override
    public List<MoneyReservation> getActiveReservations(String cardNumber) {
        return em.createNamedQuery("getActiveReservations", MoneyReservation.class)
                .setParameter("cardNumber", cardNumber)
                .setParameter("openedStatus", MoneyReservation.STATUS_OPENED)
                .getResultList();
    }

    @Override
    public double getActiveReservationSum(String cardNumber) {
        Double sum = em.createNamedQuery("getActiveReservationSum", Double.class)
                .setParameter("cardNumber", cardNumber)
                .setParameter("openedStatus", MoneyReservation.STATUS_OPENED)
                .getSingleResult();
        return (sum != null) ? sum : 0;
    }

    @Override
    public MoneyReservation createReservation(String cardNumber, double amount, PaymentTransaction paymentTransaction) throws NoEnoughMoneyException {
        PaymentCard paymentCard = paymentCardRepository.findById(cardNumber);
        double reservedSum = getActiveReservationSum(cardNumber);

        if(reservedSum + amount > paymentCard.getAmount()) {
            throw new NoEnoughMoneyException(String.format("%s (%.2f)", cardNumber, paymentCard.getAmount()), reservedSum + amount);
        }

        MoneyReservation moneyReservation = new MoneyReservation(paymentCard, MoneyReservation.STATUS_OPENED, amount);
        moneyReservation.setPaymentTransaction(paymentTransaction);
        save(moneyReservation);
        return moneyReservation;
    }

    @Override
    public void closeActiveReservation(Long id) {
        em.createNamedQuery("closeReservation")
                .setParameter("id", id)
                .setParameter("closedStatus", MoneyReservation.STATUS_CLOSED)
                .executeUpdate();
    }

    @Override
    public void closeActiveReservation(PaymentTransaction transaction) {
        closeActiveReservation(em.merge(transaction).getMoneyReservation().getId());
    }
}
