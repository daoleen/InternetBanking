package com.daoleen.banking.repository.local;

import com.daoleen.banking.domain.PaymentCard;
import com.daoleen.banking.repository.Repository;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by alex on 1/17/15.
 */
@Local
public interface PaymentCardRepository extends Repository<PaymentCard, String> {
    public List<PaymentCard> findByBankAndClientPassport(
            String bankName, String passpSeries, int passpNumber);

    public List<PaymentCard> findByUsername(String username);
    public void activateCard(String cardNumber);
    public List<PaymentCard> findInactive();
}
