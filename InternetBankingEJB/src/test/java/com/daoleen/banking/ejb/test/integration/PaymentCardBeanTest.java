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
                "hashedPass", new Date(), bank, client, true);
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

    @Test
    public void isActiveWhereClauseCorrect() {
        int initialSize = paymentCardRepository.findAll().size();

        // create and save inactive payment card
        PaymentCard paymentCard = (PaymentCard) createNewEntity();
        paymentCard.setCardNumber("4444-0000-1222-3333");
        paymentCard.setActive(false);
        paymentCardRepository.save(paymentCard);

        int resultSize = paymentCardRepository.findAll().size();
        assertEquals(initialSize, resultSize);
    }

    @Test
    public void activateCardTest() {
        String cardNumber = "2222-0000-1222-3333";
        PaymentCard paymentCard = (PaymentCard) createNewEntity();
        paymentCard.setCardNumber(cardNumber);
        paymentCard.setActive(false);
        paymentCardRepository.save(paymentCard);

        PaymentCard inactiveCard = paymentCardRepository.findById(cardNumber);
        assertTrue(inactiveCard == null || !inactiveCard.isActive());
        paymentCardRepository.activateCard(cardNumber);

        PaymentCard activeCard = paymentCardRepository.findById(cardNumber);
        assertTrue(activeCard.isActive());
    }
}
