package com.daoleen.banking.repository;

import com.daoleen.banking.domain.PaymentTransaction;

import java.util.List;
import java.util.UUID;

/**
 * Created by alex on 12/17/14.
 */
public interface PaymentTransactionRepository extends Repository<PaymentTransaction, UUID> {
    public List<PaymentTransaction> findByCardNumber(String cardNumber);
}
