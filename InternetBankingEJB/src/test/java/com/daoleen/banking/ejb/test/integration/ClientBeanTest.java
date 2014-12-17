package com.daoleen.banking.ejb.test.integration;

import com.daoleen.banking.domain.Client;
import com.daoleen.banking.domain.ClientAddress;
import com.daoleen.banking.domain.Identifiable;
import com.daoleen.banking.repository.local.ClientAddressRepository;
import com.daoleen.banking.repository.local.ClientRepository;
import org.junit.Test;

import javax.ejb.EJB;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by alex on 12/17/14.
 */
public class ClientBeanTest extends AbstractBeanTest {

    @EJB
    private ClientRepository clientRepository;

    @EJB
    private ClientAddressRepository clientAddressRepository;

    @Override
    protected ClientRepository getEntityRepository() {
        return clientRepository;
    }

    @Override
    protected Identifiable createNewEntity() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.YYYY");
        Date birthDate, regDate = new Date();
        ClientAddress address = clientAddressRepository.findAll().get(0);
        try {
            birthDate = dateFormat.parse("10.10.2010");
        } catch (ParseException e) {
            birthDate = new Date();
        }
        return new Client("Sanya", "Kozlov", "Valerevich", birthDate,
                "MP", 22222222, regDate, "2020327", address);
    }


    // 'Alexander', 'Kozlov', 'Valerevich', '2010-02-02', 'MP', 100000000, 1, CURRENT_TIME(), '217-20-20'
    // 'Alex', 'Kozlov', 'Petrovich', '2010-07-02', 'MP', 100200000, 1, CURRENT_TIME(), '200-01-02');

    @Test
    public void findByPassport() {
        Client client = clientRepository.findByPassport("MP", 100200000);
        assertNotNull(client);
    }

    @Test
    public void findByNotSetPassport() {
        Client client = clientRepository.findByPassport("UU", 100200000);
        assertNull(client);
    }

    @Test
    public void findByMobileNumber() {
        Client client = clientRepository.findByMobileNumber("217-20-20");
        assertNotNull(client);
    }

    @Test
    public void findByNotSetMobileNumber() {
        Client client = clientRepository.findByMobileNumber("000-20-20");
        assertNull(client);
    }

    @Test
    public void findByAddressId() {
        List<Client> clients = clientRepository.findByAddressId(1L);
        assertNotNull(clients);
        assertTrue(clients.size() > 0);
    }

    @Test
    public void findByNotSetAddressId() {
        List<Client> clients = clientRepository.findByAddressId(31L);
        assertNotNull(clients);
        assertEquals(0, clients.size());
    }

    @Test
    public void findByAddress() {
        List<Client> clients = clientRepository.findbyAddress("Masherova", "Minsk", 12, 17);
        assertNotNull(clients);
        assertTrue(clients.size() > 0);
    }

    @Test
    public void findByNotSetAddress() {
        List<Client> clients = clientRepository.findbyAddress("Moscovskaya", "Minsk", 12, 17);
        assertNotNull(clients);
        assertEquals(0, clients.size());
    }

    @Test
    public void findByFIO() {
        List<Client> clients = clientRepository.findByFIO("Alexander", "Kozlov", "Valerevich");
        assertNotNull(clients);
        assertTrue(clients.size() > 0);
    }

    @Test
    public void findByNotSetFIO() {
        List<Client> clients = clientRepository.findByFIO("Ivanov", "Kozlov", "Kolosov");
        assertNotNull(clients);
        assertEquals(0, clients.size());
    }
}
