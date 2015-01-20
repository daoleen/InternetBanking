package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.PaymentTransaction;
import com.daoleen.banking.repository.local.PaymentTransactionRepository;
import com.daoleen.banking.repository.remote.PaymentTransactionRepositoryRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.enterprise.inject.Typed;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by alex on 12/17/14.
 */
@Stateless
@Local(PaymentTransactionRepository.class)
@Remote(PaymentTransactionRepositoryRemote.class)
@Typed(PaymentTransactionRepository.class)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class PaymentTransactionBean extends AbstractBean<PaymentTransaction, UUID>
        implements PaymentTransactionRepository, PaymentTransactionRepositoryRemote, Serializable {
    private final static Logger logger = LoggerFactory.getLogger(PaymentTransactionBean.class);
    private static final long serialVersionUID = 931875454907930831L;

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
