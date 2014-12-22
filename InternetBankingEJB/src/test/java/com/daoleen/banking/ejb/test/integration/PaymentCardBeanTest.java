package com.daoleen.banking.ejb.test.integration;

import com.daoleen.banking.domain.Bank;
import com.daoleen.banking.domain.Client;
import com.daoleen.banking.domain.Identifiable;
import com.daoleen.banking.domain.PaymentCard;
import com.daoleen.banking.repository.BankRepository;
import com.daoleen.banking.repository.ClientRepository;
import com.daoleen.banking.repository.PaymentCardRepository;
import org.junit.Test;

import javax.ejb.EJB;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by alex on 12/17/14.
 */
public class PaymentCardBeanTest extends AbstractBeanTest {

    @EJB
    private PaymentCardRepository paymentCardRepository;

    @EJB
    private ClientRepository clientRepository;

    @EJB
    private BankRepository bankRepository;

    @Override
    protected PaymentCardRepository getEntityRepository() {
        return paymentCardRepository;
    }

    @Override
    protected Identifiable createNewEntity() {
        Bank bank = bankRepository.findAll().get(0);
        Client client = clientRepository.findAll().get(0);
        return new PaymentCard("1000-0000-0000-0002", 320.0,
                "hashedPass", new Date(), bank, client);
    }


    @Test
    public void findByBankAndClientPassport() {
        List<PaymentCard> cards =
                paymentCardRepository.findByBankAndClientPassport("БПС", "MP", 100000000);
        assertNotNull(cards);
        assertTrue(cards.size() > 0);
    }

    @Test
    public void findByBankAndClientIncorrectPassport() {
        List<PaymentCard> cards =
                paymentCardRepository.findByBankAndClientPassport("БПС", "MP", 700000007);
        assertNotNull(cards);
        assertEquals(0, cards.size());
    }
}
