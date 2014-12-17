package com.daoleen.banking.repository;

import com.daoleen.banking.domain.Bank;

import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
public interface BankRepository extends Repository<Bank, Integer> {
    public List<Bank> findByName(String name);

    public Bank findByBankAccountNumber(long bankAccountNumber);
}
