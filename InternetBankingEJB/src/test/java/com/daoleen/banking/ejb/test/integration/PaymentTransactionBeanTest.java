package com.daoleen.banking.ejb.test.integration;

import com.daoleen.banking.domain.Identifiable;
import com.daoleen.banking.domain.PaymentCard;
import com.daoleen.banking.domain.PaymentTransaction;
import com.daoleen.banking.repository.PaymentCardRepository;
import com.daoleen.banking.repository.PaymentTransactionRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
}
