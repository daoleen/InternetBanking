package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.PaymentCard;
import com.daoleen.banking.repository.local.PaymentCardRepository;
import com.daoleen.banking.repository.remote.PaymentCardRepositoryRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.enterprise.inject.Typed;
import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
@Stateless
@Local(PaymentCardRepository.class)
@Remote(PaymentCardRepositoryRemote.class)
@Typed(PaymentCardRepository.class)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class PaymentCardBean extends AbstractBean<PaymentCard, String> implements PaymentCardRepository, PaymentCardRepositoryRemote {
    private final static Logger logger = LoggerFactory.getLogger(PaymentCardBean.class);

    public PaymentCardBean() {
        super(PaymentCard.class);
    }


    @Override
    public List<PaymentCard> findAll() {
        return em.createNamedQuery(PaymentCard.FIND_ALL, PaymentCard.class).getResultList();
    }


    @Override
    public List<PaymentCard> findByBankAndClientPassport(String bankName, String passpSeries, int passpNumber) {
        return em.createNamedQuery(PaymentCard.FIND_BY_BANK_AND_CLIENT_PASSPORT, PaymentCard.class)
                .setParameter("bankName", bankName)
                .setParameter("passportSeries", passpSeries)
                .setParameter("passportNumber", passpNumber)
                .getResultList();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
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
