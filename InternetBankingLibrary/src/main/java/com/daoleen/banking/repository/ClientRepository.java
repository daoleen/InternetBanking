package com.daoleen.banking.repository;

import com.daoleen.banking.domain.Client;
import com.daoleen.banking.domain.ClientAddress;
import com.daoleen.banking.exception.ClientRegistrationException;

import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
public interface ClientRepository extends Repository<Client, Long> {
    public Client register(Client client, ClientAddress address) throws ClientRegistrationException;
    public Client findByPassport(String series, int number);
    public Client findByMobileNumber(String number);
    public List<Client> findByAddressId(long addressId);
    public List<Client> findbyAddress(String street, String city, int houseNumber, int apartmentNumber);
    public List<Client> findByFIO(String firstName, String lastName, String patronymicName);
}
