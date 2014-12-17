package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.PaymentCard;
import com.daoleen.banking.repository.PaymentCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.inject.Typed;
import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
@Stateless
@Local(PaymentCardRepository.class)
@Typed(PaymentCardRepository.class)
public class PaymentCardBean extends AbstractBean<PaymentCard, String> implements PaymentCardRepository {
    private final static Logger logger = LoggerFactory.getLogger(PaymentCardBean.class);

    public PaymentCardBean() {
        super(PaymentCard.class);
    }

    @Override
    public List<PaymentCard> findByBankAndClientPassport(String bankName, String passpSeries, int passpNumber) {
        return em.createNamedQuery("findByBankAndClientPassport", PaymentCard.class)
                .setParameter("bankName", bankName)
                .setParameter("passportSeries", passpSeries)
                .setParameter("passportNumber", passpNumber)
                .getResultList();
    }
}
