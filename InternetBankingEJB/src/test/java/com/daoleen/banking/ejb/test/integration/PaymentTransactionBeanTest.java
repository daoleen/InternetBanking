package com.daoleen.banking.ejb.test.integration;

import com.daoleen.banking.domain.Identifiable;
import com.daoleen.banking.domain.PaymentCard;
import com.daoleen.banking.domain.PaymentTransaction;
import com.daoleen.banking.enums.MoneyReservationStatus;
import com.daoleen.banking.enums.PaymentTransactionStatus;
import com.daoleen.banking.enums.ProcessingStatus;
import com.daoleen.banking.exception.NoEnoughMoneyException;
import com.daoleen.banking.exception.PaymentTransactionException;
import com.daoleen.banking.repository.PaymentCardRepository;
import com.daoleen.banking.repository.PaymentTransactionRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by alex on 12/17/14.
 */
public class PaymentTransactionBeanTest extends AbstractBeanTest {
    private final static Logger logger = LoggerFactory.getLogger(PaymentTransactionBeanTest.class);

    @EJB
    private PaymentTransactionRepository paymentTransactionRepository;

    @EJB
    private PaymentCardRepository paymentCardRepository;

    @Override
    protected PaymentTransactionRepository getEntityRepository() {
        return paymentTransactionRepository;
    }

    @Override
    protected Identifiable createNewEntity() {
        PaymentCard paymentCard = paymentCardRepository.findAll().get(0);
        PaymentTransaction paymentTransaction = new PaymentTransaction();
        paymentTransaction.setCard(paymentCard);
        return paymentTransaction;
    }


    @Test
    public void findByCardNumber() {
        List<PaymentTransaction> paymentTransactions = paymentTransactionRepository.findByCardNumber("0000-0000-0000-0001");
        assertNotNull(paymentTransactions);
        assertTrue(paymentTransactions.size() > 0);
    }


    @Test
    public void transferMoneyToCard() throws NoEnoughMoneyException, PaymentTransactionException {
        PaymentCard senderCard = paymentCardRepository.findById("0000-0000-0000-0001");
        PaymentCard recipientCard = paymentCardRepository.findById("0000-0000-0000-0002");
        double amount = 20.15;
        double senderCardAmount = senderCard.getAmount();
        double recipientCardAmount = recipientCard.getAmount();

        paymentTransactionRepository.createTransaction(1000000, senderCard.getCardNumber(), amount);
        UUID transactionId = paymentTransactionRepository.getPaymentTransaction().getId();
        paymentTransactionRepository.fillDataForCardTransfer(recipientCard.getCardNumber());
        paymentTransactionRepository.completeProcessing();

        senderCard = paymentCardRepository.findById("0000-0000-0000-0001");
        recipientCard = paymentCardRepository.findById("0000-0000-0000-0002");

        assertEquals(senderCardAmount-amount, senderCard.getAmount(), 0.001);
        assertEquals(recipientCardAmount+amount, recipientCard.getAmount(), 0.001);

        PaymentTransaction completedTransaction = paymentTransactionRepository.findById(transactionId);
        assertEquals(completedTransaction.getMoneyReservation().getStatus(), MoneyReservationStatus.CLOSED);
        assertEquals(completedTransaction.getTransactionStatus().getStatus(), PaymentTransactionStatus.CLOSED);
        assertEquals(completedTransaction.getProcessingStatus(), ProcessingStatus.SUCCESS);
    }


    @Test
    public void transferMoneyToBankAccount() throws NoEnoughMoneyException, PaymentTransactionException {
        PaymentCard senderCard = paymentCardRepository.findById("0000-0000-0000-0001");
        double amount = 4.15;
        double senderCardAmount = senderCard.getAmount();

        paymentTransactionRepository.createTransaction(1000000, senderCard.getCardNumber(), amount);
        UUID transactionId = paymentTransactionRepository.getPaymentTransaction().getId();
        paymentTransactionRepository.fillDataForMoneyTransfer("1521015", "Alexander", "Kozlov", "Test");
        paymentTransactionRepository.completeProcessing();

        senderCard = paymentCardRepository.findById("0000-0000-0000-0001");
        assertEquals(senderCardAmount-amount, senderCard.getAmount(), 0.001);

        PaymentTransaction completedTransaction = paymentTransactionRepository.findById(transactionId);
        assertEquals(completedTransaction.getMoneyReservation().getStatus(), MoneyReservationStatus.CLOSED);
        assertEquals(completedTransaction.getTransactionStatus().getStatus(), PaymentTransactionStatus.CLOSED);
        assertEquals(completedTransaction.getProcessingStatus(), ProcessingStatus.SUCCESS);
    }


    @Test(expected = NoEnoughMoneyException.class)
    public void transferMoneyWithNoEnoughMoneyException() throws NoEnoughMoneyException, PaymentTransactionException {
        PaymentCard senderCard = paymentCardRepository.findById("0000-0000-0000-0001");
        PaymentCard recipientCard = paymentCardRepository.findById("0000-0000-0000-0002");
        double senderCardAmount = senderCard.getAmount();
        double recipientCardAmount = recipientCard.getAmount();
        double amount = senderCardAmount + 0.00001;
        UUID transactionId = null;

        try {
            paymentTransactionRepository.createTransaction(1000000, senderCard.getCardNumber(), amount);
            transactionId = paymentTransactionRepository.getPaymentTransaction().getId();
            paymentTransactionRepository.fillDataForCardTransfer(recipientCard.getCardNumber());
            paymentTransactionRepository.completeProcessing();
        }
        catch (NoEnoughMoneyException | PaymentTransactionException e) {
            logger.info("An NoEnoughMoneyException or PaymentTransactionException was occurred while transfer money. " +
                    "The NoEnoughMoneyException is expecting. Error message: {}", e.getStackTrace().toString());
            throw e;
        }
        finally {
            senderCard = paymentCardRepository.findById("0000-0000-0000-0001");
            recipientCard = paymentCardRepository.findById("0000-0000-0000-0002");

            assertEquals(senderCardAmount, senderCard.getAmount(), 0.001);
            assertEquals(recipientCardAmount, recipientCard.getAmount(), 0.001);

            if(transactionId != null) {
                PaymentTransaction completedTransaction = paymentTransactionRepository.findById(transactionId);
                assertEquals(completedTransaction.getMoneyReservation().getStatus(), MoneyReservationStatus.CLOSED);
                assertEquals(completedTransaction.getTransactionStatus().getStatus(), PaymentTransactionStatus.CLOSED);
                assertEquals(completedTransaction.getProcessingStatus(), ProcessingStatus.FAIL);
            }
        }
    }
}
