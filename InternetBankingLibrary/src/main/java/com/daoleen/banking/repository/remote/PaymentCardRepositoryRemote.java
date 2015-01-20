package com.daoleen.banking.repository.remote;

import com.daoleen.banking.domain.PaymentCard;
import com.daoleen.banking.repository.Repository;

import javax.ejb.Remote;
import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
@Remote
public interface PaymentCardRepositoryRemote extends Repository<PaymentCard, String> {
    public List<PaymentCard> findByBankAndClientPassport(
            String bankName, String passpSeries, int passpNumber);

    public List<PaymentCard> findByUsername(String username);

    public void activateCard(String cardNumber);

    public List<PaymentCard> findInactive();
}
