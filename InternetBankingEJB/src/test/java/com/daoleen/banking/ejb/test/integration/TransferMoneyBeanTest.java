package com.daoleen.banking.ejb.test.integration;

import com.daoleen.banking.domain.PaymentCard;
import com.daoleen.banking.domain.PaymentTransaction;
import com.daoleen.banking.enums.MoneyReservationStatus;
import com.daoleen.banking.enums.PaymentTransactionStatus;
import com.daoleen.banking.enums.ProcessingStatus;
import com.daoleen.banking.exception.NoEnoughMoneyException;
import com.daoleen.banking.exception.PaymentTransactionException;
import com.daoleen.banking.repository.remote.PaymentCardRepositoryRemote;
import com.daoleen.banking.repository.remote.PaymentTransactionRepositoryRemote;
import com.daoleen.banking.repository.remote.TransferMoneyRepositoryRemote;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by alex on 1/14/15.
 */
public class TransferMoneyBeanTest extends AbstractIntegrationTest {
    private final static Logger logger = LoggerFactory.getLogger(TransferMoneyBeanTest.class);

    @EJB
    private PaymentCardRepositoryRemote paymentCardRepository;

    @EJB
    private PaymentTransactionRepositoryRemote paymentTransactionRepository;

    @EJB
    private TransferMoneyRepositoryRemote transferMoneyRepository;



    @Test
    public void transferMoneyToCard() throws NoEnoughMoneyException, PaymentTransactionException {
        PaymentCard senderCard = paymentCardRepository.findById("0000-0000-0000-0001");
        PaymentCard recipientCard = paymentCardRepository.findById("0000-0000-0000-0002");
        double amount = 20.15;
        double senderCardAmount = senderCard.getAmount();
        double recipientCardAmount = recipientCard.getAmount();

        transferMoneyRepository.createTransaction(1000000, senderCard.getCardNumber(), amount);
        UUID transactionId = transferMoneyRepository.getPaymentTransaction().getId();
        transferMoneyRepository.fillDataForCardTransfer(recipientCard.getCardNumber());
        transferMoneyRepository.completeProcessing();

        senderCard = paymentCardRepository.findById("0000-0000-0000-0001");
        recipientCard = paymentCardRepository.findById("0000-0000-0000-0002");

        assertEquals(senderCardAmount-amount, senderCard.getAmount(), 0.001);
        assertEquals(recipientCardAmount+amount, recipientCard.getAmount(), 0.001);

        PaymentTransaction completedTransaction = paymentTransactionRepository.findById(transactionId);

        assertTrue(completedTransaction.getMoneyReservation().getStatus().equals(MoneyReservationStatus.CLOSED));
        assertEquals("The PaymentTransactionStatus property must be set to CLOSED", PaymentTransactionStatus.CLOSED,
                completedTransaction.getTransactionStatus());
        assertEquals("The ProcessingStatus property must be set to SUCCESS", ProcessingStatus.SUCCESS,
                completedTransaction.getProcessingStatus());
    }


    @Test
    public void transferMoneyToBankAccount() throws NoEnoughMoneyException, PaymentTransactionException {
        PaymentCard senderCard = paymentCardRepository.findById("0000-0000-0000-0001");
        double amount = 4.15;
        double senderCardAmount = senderCard.getAmount();

        transferMoneyRepository.createTransaction(1000000, senderCard.getCardNumber(), amount);
        UUID transactionId = transferMoneyRepository.getPaymentTransaction().getId();
        transferMoneyRepository.fillDataForMoneyTransfer("1521015", "Alexander", "Kozlov", "Test");
        transferMoneyRepository.completeProcessing();

        senderCard = paymentCardRepository.findById("0000-0000-0000-0001");
        assertEquals(senderCardAmount-amount, senderCard.getAmount(), 0.001);

        PaymentTransaction completedTransaction = paymentTransactionRepository.findById(transactionId);

        assertTrue(completedTransaction.getMoneyReservation().getStatus().equals(MoneyReservationStatus.CLOSED));
        assertEquals("The PaymentTransactionStatus property must be set to CLOSED", PaymentTransactionStatus.CLOSED,
                completedTransaction.getTransactionStatus());
        assertEquals("The ProcessingStatus property must be set to SUCCESS", ProcessingStatus.SUCCESS,
                completedTransaction.getProcessingStatus());
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
            transferMoneyRepository.createTransaction(1000000, senderCard.getCardNumber(), amount);
            transactionId = transferMoneyRepository.getPaymentTransaction().getId();
            transferMoneyRepository.fillDataForCardTransfer(recipientCard.getCardNumber());
            transferMoneyRepository.completeProcessing();
        }
        catch (NoEnoughMoneyException | PaymentTransactionException e) {
            logger.info("An NoEnoughMoneyException or PaymentTransactionException was occurred while transfer money. " +
                    "The NoEnoughMoneyException is expecting. Error message: {}", e.getStackTrace().toString());
            throw e;
        }
        finally {
            System.out.println("FINALLY BLOCK");
            senderCard = paymentCardRepository.findById("0000-0000-0000-0001");
            recipientCard = paymentCardRepository.findById("0000-0000-0000-0002");

            assertEquals(senderCardAmount, senderCard.getAmount(), 0.001);
            assertEquals(recipientCardAmount, recipientCard.getAmount(), 0.001);

            if(transactionId != null) {
                PaymentTransaction completedTransaction = paymentTransactionRepository.findById(transactionId);
                assertTrue(completedTransaction.getMoneyReservation().getStatus().equals(MoneyReservationStatus.CLOSED));
                assertEquals("The PaymentTransactionStatus property must be set to CLOSED", PaymentTransactionStatus.CLOSED,
                        completedTransaction.getTransactionStatus());
                assertEquals("The ProcessingStatus property must be set to FAIL", ProcessingStatus.FAIL,
                        completedTransaction.getProcessingStatus());
            }
        }
    }

}
