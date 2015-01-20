package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.Bank;
import com.daoleen.banking.repository.local.BankRepository;
import com.daoleen.banking.repository.remote.BankRepositoryRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.enterprise.inject.Typed;
import javax.persistence.NoResultException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
@Stateless
@Local(BankRepository.class)
@Typed(BankRepository.class)
@Remote(BankRepositoryRemote.class)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class BankBean extends AbstractBean<Bank, Integer> implements Serializable, BankRepository, BankRepositoryRemote {
    private final static Logger logger = LoggerFactory.getLogger(BankBean.class);
    private static final long serialVersionUID = 4216171919563545947L;

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
