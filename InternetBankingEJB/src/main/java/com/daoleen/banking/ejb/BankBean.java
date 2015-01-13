package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.Bank;
import com.daoleen.banking.repository.BankRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.inject.Typed;
import javax.persistence.NoResultException;
import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
@Stateless
@Local(BankRepository.class)
@Typed(BankRepository.class)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class BankBean extends AbstractBean<Bank, Integer> implements BankRepository {
    private final static Logger logger = LoggerFactory.getLogger(BankBean.class);

    public BankBean() {
        super(Bank.class);
    }

    @Override
    public List<Bank> findByName(String name) {
        return em.createQuery(
                "select b from Bank b where lower(b.name) like '%" + name.toLowerCase() + "%'",
                Bank.class
        ).getResultList();
    }

    @Override
    public Bank findByBankAccountNumber(long bankAccountNumber) {
        try {
            return em.createNamedQuery(Bank.FIND_BY_BANK_ACCOUNT_NUMBER, Bank.class)
                    .setParameter("bankAccountNumber", bankAccountNumber)
                    .getSingleResult();
        } catch (NoResultException ex) {
            logger.info("Bank with {} account was not found.", bankAccountNumber);
        }
        return null;
    }
}
