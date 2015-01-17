package com.daoleen.banking.repository.local;

import com.daoleen.banking.domain.Client;
import com.daoleen.banking.domain.ClientAddress;
import com.daoleen.banking.exception.ClientRegistrationException;
import com.daoleen.banking.repository.Repository;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by alex on 1/17/15.
 */
@Local
public interface ClientRepository extends Repository<Client, Long> {
    public Client register(Client client, ClientAddress address) throws ClientRegistrationException;
    public Client findByPassport(String series, int number);
    public Client findByMobileNumber(String number);
    public List<Client> findByAddressId(long addressId);
    public List<Client> findbyAddress(String street, String city, int houseNumber, int apartmentNumber);
    public List<Client> findByFIO(String firstName, String lastName, String patronymicName);
}

