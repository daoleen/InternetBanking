package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.PaymentTransaction;
import com.daoleen.banking.repository.PaymentTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.inject.Typed;
import java.util.List;
import java.util.UUID;

/**
 * Created by alex on 12/17/14.
 */
@Stateless
@Local(PaymentTransactionRepository.class)
@Typed(PaymentTransactionRepository.class)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class PaymentTransactionBean extends AbstractBean<PaymentTransaction, UUID> implements PaymentTransactionRepository {
    private final static Logger logger = LoggerFactory.getLogger(PaymentTransactionBean.class);

    public PaymentTransactionBean() {
        super(PaymentTransaction.class);
    }


    @Override
    public List<PaymentTransaction> findByCardNumber(String cardNumber) {
        return em.createNamedQuery(PaymentTransaction.FIND_BY_CARD_NUMBER, PaymentTransaction.class)
                .setParameter("cardNumber", cardNumber)
                .getResultList();
    }
}
