package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.MoneyReservation;
import com.daoleen.banking.domain.PaymentCard;
import com.daoleen.banking.domain.PaymentTransaction;
import com.daoleen.banking.enums.MoneyReservationStatus;
import com.daoleen.banking.enums.PaymentTransactionStatus;
import com.daoleen.banking.enums.ProcessingStatus;
import com.daoleen.banking.exception.NoEnoughMoneyException;
import com.daoleen.banking.exception.PaymentTransactionException;
import com.daoleen.banking.repository.MoneyReservationRepository;
import com.daoleen.banking.repository.PaymentCardRepository;
import com.daoleen.banking.repository.PaymentTransactionRepository;
import com.daoleen.banking.repository.TransferMoneyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.inject.Typed;
import javax.transaction.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by alex on 1/14/15.
 */
@Stateful
@Local(TransferMoneyRepository.class)
@Typed(TransferMoneyRepository.class)
@StatefulTimeout(value = 10, unit = TimeUnit.MINUTES)
@TransactionManagement(TransactionManagementType.BEAN)
public class TransferMoneyBean implements TransferMoneyRepository {
    private final static Logger logger = LoggerFactory.getLogger(PaymentTransactionBean.class);
    private PaymentTransaction paymentTransaction;


    @EJB
    private PaymentCardRepository paymentCardRepository;

    @EJB
    private MoneyReservationRepository moneyReservationRepository;

    @EJB
    private PaymentTransactionRepository paymentTransactionRepository;

    @Resource
    private UserTransaction tx;



    @Override
    public void createTransaction(int recipientBankId, String senderCardNumber, double amount)
            throws PaymentTransactionException, NoEnoughMoneyException
    {
        PaymentCard senderPaymentCard = paymentCardRepository.findById(senderCardNumber);

        if(senderPaymentCard == null) {
            throw new PaymentTransactionException("senderCardNumber is null");
        }

        this.paymentTransaction = new PaymentTransaction(senderPaymentCard);
        this.paymentTransaction.setRecepientBankId(recipientBankId);
        this.paymentTransaction.setTransactionStatus(PaymentTransactionStatus.FILLING_DATA);
        this.paymentTransaction.setProcessingStatus(ProcessingStatus.NOT_READY);

        try {
            tx.begin();
            this.paymentTransaction = paymentTransactionRepository.save(this.paymentTransaction);
            MoneyReservation moneyReservation = moneyReservationRepository.createReservation(senderPaymentCard,
                    amount, this.paymentTransaction);
            this.paymentTransaction.setMoneyReservation(moneyReservation);
            tx.commit();
        } catch (NoEnoughMoneyException e) {
            logger.error("An exception occurred while creating money reservation and payment transaction. The transaction will rolled back");
            // TODO: sessionContext.setRollbackOnly();
            try {
                tx.rollback();
            } catch (SystemException e1) {
                e1.printStackTrace();
            }
            throw e;
        } catch (HeuristicRollbackException e) {
            e.printStackTrace();
        } catch (RollbackException e) {
            e.printStackTrace();
        } catch (SystemException e) {
            e.printStackTrace();
        } catch (HeuristicMixedException e) {
            e.printStackTrace();
        } catch (NotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fillDataForCardTransfer(String recipientCardNumber) {
        PaymentCard recipientCard = paymentCardRepository.findById(recipientCardNumber);
        this.paymentTransaction.setRecepientCard(recipientCard);
        this.paymentTransaction.setTransactionStatus(PaymentTransactionStatus.PROCESSING);
    }

    @Override
    public void fillDataForMoneyTransfer(String rcpAccountNumber, String rcpFirstName, String rcpLastName, String rcpPatronymicName) {
        this.paymentTransaction.setRecepientAccountNumber(rcpAccountNumber);
        this.paymentTransaction.setRecepientFirstName(rcpFirstName);
        this.paymentTransaction.setRecepientLastName(rcpLastName);
        this.paymentTransaction.setRecepientPatronymicName(rcpPatronymicName);
        this.paymentTransaction.setTransactionStatus(PaymentTransactionStatus.PROCESSING);
    }

    @Remove
    @Override
    public void completeProcessing() throws PaymentTransactionException {
        try {
            // отнять денег у плательщика
            tx.begin();
            double amount = paymentTransaction.getMoneyReservation().getAmount();
            paymentTransaction.getCard().setAmount(paymentTransaction.getCard().getAmount() - amount);

            // transfer money to a card
            if(paymentTransaction.getRecepientCard() != null) {
                paymentTransaction.getRecepientCard().setAmount(paymentTransaction.getRecepientCard().getAmount() + amount);
            }

            //moneyReservationRepository.closeActiveReservation(paymentTransaction.getMoneyReservation().getId());
            this.paymentTransaction.getMoneyReservation().setStatus(MoneyReservationStatus.CLOSED);
            //this.paymentTransaction.getMoneyReservation().setStatus(MoneyReservationStatus.CLOSED);
            this.paymentTransaction.setProcessingStatus(ProcessingStatus.SUCCESS);
            this.paymentTransaction.setTransactionStatus(PaymentTransactionStatus.CLOSED);
            this.paymentTransaction = paymentTransactionRepository.save(this.paymentTransaction);
            tx.commit();
        }
        catch (Exception e) {
            logger.error("An exception occurred while closing money transaction. The transaction will be rolled back. Error: {}", e.getStackTrace().toString());
            System.out.println("An exception occurred while closing money transaction. The transaction will be rolled back. Error: " + e.getStackTrace().toString());
            try {
                tx.rollback();
            }
            catch (SystemException se) {
                logger.error("An SystemException was occurred while transaction rolling back. Error: {}", se.getStackTrace().toString());
                System.out.println("An SystemException was occurred while transaction rolling back. Error: " + se.getStackTrace().toString());
            }
            finally {
                paymentTransaction = paymentTransactionRepository.findById(paymentTransaction.getId());
                paymentTransaction.setProcessingStatus(ProcessingStatus.FAIL);
                paymentTransaction.getMoneyReservation().setStatus(MoneyReservationStatus.CLOSED);
                paymentTransaction.setTransactionStatus(PaymentTransactionStatus.CLOSED);
                throw new PaymentTransactionException(e.getMessage());
            }
        }
    }


    @Override
    public final PaymentTransaction getPaymentTransaction() {
        return paymentTransaction;
    }
}
