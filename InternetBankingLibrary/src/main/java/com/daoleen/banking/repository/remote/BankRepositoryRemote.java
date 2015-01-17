package com.daoleen.banking.repository.remote;

import com.daoleen.banking.domain.Bank;
import com.daoleen.banking.repository.Repository;

import javax.ejb.Remote;
import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
@Remote
public interface BankRepositoryRemote extends Repository<Bank, Integer> {
    public List<Bank> findByName(String name);

    public Bank findByBankAccountNumber(long bankAccountNumber);
}
