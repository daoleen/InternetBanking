package com.daoleen.banking.repository.remote;

import com.daoleen.banking.domain.ClientAddress;
import com.daoleen.banking.repository.Repository;

import javax.ejb.Remote;
import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
@Remote
public interface ClientAddressRepositoryRemote extends Repository<ClientAddress, Long> {
    public List<ClientAddress> findByAddress(String street, int cityId, int houseNumber, int apartmentNumber);
}
