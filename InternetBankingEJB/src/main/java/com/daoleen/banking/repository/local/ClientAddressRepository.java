package com.daoleen.banking.repository.local;

import com.daoleen.banking.domain.ClientAddress;
import com.daoleen.banking.repository.Repository;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by alex on 1/17/15.
 */
@Local
public interface ClientAddressRepository extends Repository<ClientAddress, Long> {
    public List<ClientAddress> findByAddress(String street, int cityId, int houseNumber, int apartmentNumber);

    public List<ClientAddress> findByAddress(String street, String cityName, int houseNumber, int apartmentNumber);
}
