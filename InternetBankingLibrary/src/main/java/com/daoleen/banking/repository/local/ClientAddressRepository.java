package com.daoleen.banking.repository.local;

import com.daoleen.banking.domain.ClientAddress;

import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
public interface ClientAddressRepository extends Repository<ClientAddress, Long> {
    public List<ClientAddress> findByAddress(String street, int cityId, int houseNumber, int apartmentNumber);
}
