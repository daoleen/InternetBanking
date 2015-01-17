package com.daoleen.banking.repository.local;

import com.daoleen.banking.domain.PaymentTransaction;
import com.daoleen.banking.repository.Repository;

import javax.ejb.Local;
import java.util.List;
import java.util.UUID;

/**
 * Created by alex on 1/17/15.
 */
@Local
public interface PaymentTransactionRepository extends Repository<PaymentTransaction, UUID> {
    public List<PaymentTransaction> findByCardNumber(String cardNumber);
}

