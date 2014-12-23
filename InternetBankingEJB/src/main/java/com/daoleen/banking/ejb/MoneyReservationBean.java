package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.MoneyReservation;
import com.daoleen.banking.domain.PaymentCard;
import com.daoleen.banking.domain.PaymentTransaction;
import com.daoleen.banking.enums.MoneyReservationStatus;
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
                .setParameter("openedStatus", MoneyReservationStatus.OPENED)
                .getResultList();
    }

    @Override
    public double getActiveReservationSum(PaymentCard card) {
        Double sum = em.createNamedQuery("getActiveReservationSum", Double.class)
                .setParameter("card", card)
                .setParameter("openedStatus", MoneyReservationStatus.OPENED)
                .getSingleResult();
        return (sum != null) ? sum : 0;
    }


    private MoneyReservation getMoneyReservation(PaymentCard card, double amount) throws NoEnoughMoneyException {
        double reservedSum = getActiveReservationSum(card);

        if(reservedSum + amount > card.getAmount()) {
            throw new NoEnoughMoneyException(String.format("%s (%.2f)", card.getCardNumber(), card.getAmount()), reservedSum + amount);
        }

        return new MoneyReservation(card, amount);
    }

    @Override
    public MoneyReservation createReservation(PaymentCard card, double amount, PaymentTransaction paymentTransaction)
            throws NoEnoughMoneyException {
        MoneyReservation moneyReservation = getMoneyReservation(card, amount);
        moneyReservation.setPaymentTransaction(paymentTransaction);
        return save(moneyReservation);
    }

    @Override
    public void closeActiveReservation(Long id) {
        em.createNamedQuery("closeReservation")
                .setParameter("id", id)
                .setParameter("closedStatus", MoneyReservationStatus.CLOSED)
                .executeUpdate();
    }

    @Override
    public void closeActiveReservation(PaymentTransaction transaction) {
        closeActiveReservation(transaction.getMoneyReservation().getId());
    }
}
