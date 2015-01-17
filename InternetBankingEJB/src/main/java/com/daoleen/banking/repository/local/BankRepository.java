package com.daoleen.banking.repository.local;

import com.daoleen.banking.domain.Bank;
import com.daoleen.banking.repository.Repository;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by alex on 1/17/15.
 */
@Local
public interface BankRepository extends Repository<Bank, Integer> {
    public List<Bank> findByName(String name);
    public Bank findByBankAccountNumber(long bankAccountNumber);
}