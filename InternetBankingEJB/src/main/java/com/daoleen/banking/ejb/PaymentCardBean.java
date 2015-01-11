package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.PaymentCard;
import com.daoleen.banking.repository.PaymentCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.inject.Typed;
import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
@Stateless
@Local(PaymentCardRepository.class)
@Typed(PaymentCardRepository.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PaymentCardBean extends AbstractBean<PaymentCard, String> implements PaymentCardRepository {
    private final static Logger logger = LoggerFactory.getLogger(PaymentCardBean.class);

    public PaymentCardBean() {
        super(PaymentCard.class);
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<PaymentCard> findAll() {
        return em.createNamedQuery(PaymentCard.FIND_ALL, PaymentCard.class).getResultList();
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<PaymentCard> findByBankAndClientPassport(String bankName, String passpSeries, int passpNumber) {
        return em.createNamedQuery(PaymentCard.FIND_BY_BANK_AND_CLIENT_PASSPORT, PaymentCard.class)
                .setParameter("bankName", bankName)
                .setParameter("passportSeries", passpSeries)
                .setParameter("passportNumber", passpNumber)
                .getResultList();
    }

    @Override
    public void activateCard(String cardNumber) {
        em.createNamedQuery(PaymentCard.ACTIVATE)
                .setParameter("cardNumber", cardNumber)
                .executeUpdate();
    }

    @Override
    public List<PaymentCard> findInactive() {
        return em.createNamedQuery(PaymentCard.FIND_INACTIVE, PaymentCard.class).getResultList();
    }
}
