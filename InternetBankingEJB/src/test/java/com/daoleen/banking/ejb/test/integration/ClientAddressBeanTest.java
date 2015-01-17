package com.daoleen.banking.ejb.test.integration;

import com.daoleen.banking.domain.ClientAddress;
import com.daoleen.banking.domain.Identifiable;
import com.daoleen.banking.repository.remote.CityRepositoryRemote;
import com.daoleen.banking.repository.remote.ClientAddressRepositoryRemote;
import org.junit.Test;

import javax.ejb.EJB;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by alex on 12/17/14.
 */
public class ClientAddressBeanTest extends AbstractBeanTest {

    @EJB
    private ClientAddressRepositoryRemote addressRepository;

    @EJB
    private CityRepositoryRemote cityRepository;

    @Override
    protected ClientAddressRepositoryRemote getEntityRepository() {
        return addressRepository;
    }

    @Override
    protected Identifiable createNewEntity() {
        ClientAddress address = new ClientAddress();
        address.setStreet("Kolasa");
        address.setHouseNumber(12);
        address.setApartmentNumber(10);
        address.setCity(cityRepository.findAll().get(0));
        return address;
    }


    @Test
    public void findByAddress() {
        // VALUES (1, 'Masherova', 12, NULL, 17);
        List<ClientAddress> addresses = addressRepository.findByAddress("Masherova", 1, 12, 17);
        assertEquals(1, addresses.size());
    }

    @Test
    public void findByNoSetAddress() {
        List<ClientAddress> addresses = addressRepository.findByAddress("Moscovskaya", 192, 12, 17);
        assertNotNull(addresses);
        assertEquals(0, addresses.size());
    }
}
