package com.daoleen.banking.repository.remote;

import com.daoleen.banking.domain.PaymentTransaction;
import com.daoleen.banking.repository.Repository;

import javax.ejb.Remote;
import java.util.List;
import java.util.UUID;

/**
 * Created by alex on 12/17/14.
 */
@Remote
public interface PaymentTransactionRepositoryRemote extends Repository<PaymentTransaction, UUID> {
    public List<PaymentTransaction> findByCardNumber(String cardNumber);
}
