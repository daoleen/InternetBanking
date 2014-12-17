package com.daoleen.banking.ejb.test.integration;

import com.daoleen.banking.domain.Bank;
import com.daoleen.banking.domain.Identifiable;
import com.daoleen.banking.repository.local.BankRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by alex on 12/17/14.
 */
public class BankBeanTest extends AbstractBeanTest {
    private final static Logger logger = LoggerFactory.getLogger(BankBeanTest.class);

    @EJB
    private BankRepository bankRepository;

    @Override
    protected BankRepository getEntityRepository() {
        return bankRepository;
    }

    @Override
    protected Identifiable createNewEntity() {
        return new Bank(77777777, "Belarusbank", 88888888888888888L);
    }

//VALUES (1000000, 'БПС', 100000000000000000001);

    @Test
    public void findByBankAccountNumber() {
        Bank bank = bankRepository.findByBankAccountNumber(100000000000000001L);
        assertNotNull(bank);
    }

    @Test
    public void findByNotSetBankAccountNumber() {
        Bank bank = bankRepository.findByBankAccountNumber(444L);
        assertNull(bank);
    }

    @Test
    public void findByName() {
        String name = "БПС";
        List<Bank> banks = bankRepository.findByName(name);
        assertNotNull(banks);
        assertEquals(1, banks.size());
    }

    @Test
    public void findByNotSetName() {
        List<Bank> banks = bankRepository.findByName("YYYYYYYY");
        assertNotNull(banks);
        assertEquals(0, banks.size());
    }

    @Test
    public void findFewBanksByPartOfName() {
        Bank tempLate = new Bank(10, "TempLate", 333L);
        Bank temporary = new Bank(11, "temporary", 444L);
        Bank emplaTeR = new Bank(12, "emplaTeR", 555L);
        Bank Empala = new Bank(13, "Empala", 666L);
        Bank belb = new Bank(14, "Belarusbank", 777L);
        bankRepository.save(tempLate);
        bankRepository.save(temporary);
        bankRepository.save(emplaTeR);
        bankRepository.save(Empala);
        bankRepository.save(belb);

        List<Bank> banks = bankRepository.findByName("EMP");
        assertNotNull(banks);
        assertEquals(4, banks.size());
    }
}
