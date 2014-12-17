package com.daoleen.banking.repository;

import com.daoleen.banking.domain.PaymentCard;

import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
public interface PaymentCardRepository extends Repository<PaymentCard, String> {
    public List<PaymentCard> findByBankAndClientPassport(
            String bankName, String passpSeries, int passpNumber);
}
