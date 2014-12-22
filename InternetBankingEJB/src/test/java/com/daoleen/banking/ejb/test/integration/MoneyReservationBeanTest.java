package com.daoleen.banking.ejb.test.integration;

import com.daoleen.banking.domain.Identifiable;
import com.daoleen.banking.domain.MoneyReservation;
import com.daoleen.banking.domain.PaymentCard;
import com.daoleen.banking.domain.PaymentTransaction;
import com.daoleen.banking.exception.NoEnoughMoneyException;
import com.daoleen.banking.repository.MoneyReservationRepository;
import com.daoleen.banking.repository.PaymentCardRepository;
import com.daoleen.banking.repository.PaymentTransactionRepository;
import org.junit.Test;

import javax.ejb.EJB;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by alex on 12/21/14.
 */
public class MoneyReservationBeanTest extends AbstractBeanTest {

    @EJB
    private MoneyReservationRepository moneyReservationRepository;

    @EJB
    private PaymentCardRepository paymentCardRepository;

    @EJB
    private PaymentTransactionRepository paymentTransactionRepository;

    @Override
    protected MoneyReservationRepository getEntityRepository() {
        return moneyReservationRepository;
    }

    @Override
    protected Identifiable createNewEntity() {
        List<PaymentCard> all = paymentCardRepository.findAll();
        return new MoneyReservation(all.get(0), MoneyReservation.STATUS_OPENED, 12.5);
    }


    @Test
    public void getActiveReservations() {
        PaymentCard card = paymentCardRepository.findAll().get(0);
        MoneyReservation activeReservation = new MoneyReservation(card, MoneyReservation.STATUS_OPENED, 12.5);
        MoneyReservation closedReservation = new MoneyReservation(card, MoneyReservation.STATUS_CLOSED, 12.5);
        int initialCount = moneyReservationRepository.getActiveReservations(card.getCardNumber()).size();
        int allInitialCount = moneyReservationRepository.findAll().size();
        moneyReservationRepository.save(activeReservation);
        moneyReservationRepository.save(closedReservation);
        int updatedCount = moneyReservationRepository.getActiveReservations(card.getCardNumber()).size();
        int allUpdatedCount = moneyReservationRepository.findAll().size();
        assertEquals(allInitialCount+2, allUpdatedCount);
        assertEquals(initialCount+1, updatedCount);
    }

    @Test
    public void getZeroActiveReservationSum() {
        double activeReservationSum = moneyReservationRepository.getActiveReservationSum("0000-0000-0000-0002");
        assertEquals(0f, activeReservationSum, 0.001);
    }

    @Test
    public void closeActiveReservation() {
        int initialSize = moneyReservationRepository.findAll().size();
        moneyReservationRepository.closeActiveReservation(1L);
        int updatedSize = moneyReservationRepository.findAll().size();
        assertEquals(initialSize, updatedSize); // size doesn't change
        MoneyReservation moneyReservation = moneyReservationRepository.findById(1L);
        assertEquals(MoneyReservation.STATUS_CLOSED, moneyReservation.getStatus());
    }

    @Test
    public void closeActiveReservationByPaymentTransaction() {
        PaymentTransaction paymentTransaction = moneyReservationRepository.findById(1L).getPaymentTransaction();
        moneyReservationRepository.closeActiveReservation(paymentTransaction);
        MoneyReservation moneyReservation = moneyReservationRepository.findById(1L);
        assertEquals(MoneyReservation.STATUS_CLOSED, moneyReservation.getStatus());
    }


    @Test
    public void createReservation() throws NoEnoughMoneyException {
        PaymentCard card = paymentCardRepository.findById("0000-0000-0000-0001");
        int initialSize = moneyReservationRepository.getActiveReservations(card.getId()).size();
        PaymentTransaction paymentTransaction = new PaymentTransaction(card);
        paymentTransactionRepository.save(paymentTransaction);
        moneyReservationRepository.createReservation("0000-0000-0000-0001", 1.0, paymentTransaction);
        int updatedSize = moneyReservationRepository.getActiveReservations("0000-0000-0000-0001").size();
        assertEquals(initialSize+1, updatedSize);
    }

    @Test(expected = NoEnoughMoneyException.class)
    public void noEnoughMoneyForReservation() throws NoEnoughMoneyException {
        PaymentCard card = paymentCardRepository.findById("0000-0000-0000-0001");
        double activeReservationSum = moneyReservationRepository.getActiveReservationSum("0000-0000-0000-0001");
        double amount = card.getAmount().doubleValue();
        double residual = amount - activeReservationSum;
        PaymentTransaction paymentTransaction = new PaymentTransaction(card);
        paymentTransactionRepository.save(paymentTransaction);
        moneyReservationRepository.createReservation("0000-0000-0000-0001", residual + 0.0001, paymentTransaction);
    }

    @Test
    public void getActiveReservationsSum() throws NoEnoughMoneyException {
        PaymentCard card2 = paymentCardRepository.findById("0000-0000-0000-0002");
        PaymentTransaction paymentTransactionActive = new PaymentTransaction(card2);
        paymentTransactionRepository.save(paymentTransactionActive);
        MoneyReservation reservationActive = moneyReservationRepository.createReservation(card2.getCardNumber(), 2.3, paymentTransactionActive);
        reservationActive.setStatus(MoneyReservation.STATUS_OPENED);
        moneyReservationRepository.save(reservationActive);

        PaymentTransaction paymentTransactionClosed = new PaymentTransaction(card2);
        paymentTransactionClosed.setTransactionStatus("closed");
        paymentTransactionRepository.save(paymentTransactionClosed);
        MoneyReservation reservationClosed = moneyReservationRepository.createReservation(card2.getCardNumber(), 56.7, paymentTransactionClosed);
        reservationClosed.setStatus(MoneyReservation.STATUS_CLOSED);
        moneyReservationRepository.save(reservationClosed);

        List<MoneyReservation> activeReservations = moneyReservationRepository.getActiveReservations(card2.getCardNumber());
        assertEquals(1, activeReservations.size());
        double activeReservationSum = moneyReservationRepository.getActiveReservationSum(card2.getCardNumber());
        assertEquals(2.3, activeReservationSum, 0.001);
    }
}
