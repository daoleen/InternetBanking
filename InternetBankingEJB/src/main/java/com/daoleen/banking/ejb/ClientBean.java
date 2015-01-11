package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.Client;
import com.daoleen.banking.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.inject.Typed;
import javax.persistence.NoResultException;
import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
@Stateless
@Local(ClientRepository.class)
@Typed(ClientRepository.class)
public class ClientBean extends AbstractBean<Client, Long> implements ClientRepository {
    private final static Logger logger = LoggerFactory.getLogger(ClientBean.class);

    public ClientBean() {
        super(Client.class);
    }

    @Override
    public Client findByPassport(String series, int number) {
        try {
            return em.createNamedQuery(Client.FIND_BY_PASSPORT, Client.class)
                    .setParameter("series", series)
                    .setParameter("number", number)
                    .getSingleResult();
        } catch (NoResultException ex) {
            logger.info("Client with passport [{}{}] has not been found", series, number);
        }
        return null;
    }

    @Override
    public Client findByMobileNumber(String number) {
        try {
            return em.createNamedQuery(Client.FIND_BY_MOBILE_NUMBER, Client.class)
                    .setParameter("mobile", number)
                    .getSingleResult();
        } catch (NoResultException ex) {
            logger.info("Client with mobile '{}' has not been found.", number);
        }
        return null;
    }

    @Override
    public List<Client> findByAddressId(long addressId) {
        return em.createNamedQuery(Client.FIND_BY_ADDRESS_ID, Client.class)
                .setParameter("addressId", addressId)
                .getResultList();
    }

    @Override
    public List<Client> findbyAddress(String street, String city, int houseNumber, int apartmentNumber) {
        return em.createNamedQuery(Client.FIND_BY_ADDRESS, Client.class)
                .setParameter("city", city)
                .setParameter("street", street)
                .setParameter("houseNumber", houseNumber)
                .setParameter("apartmentNumber", apartmentNumber)
                .getResultList();
    }

    @Override
    public List<Client> findByFIO(String firstName, String lastName, String patronymicName) {
        return em.createNamedQuery(Client.FIND_BY_FIO, Client.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .setParameter("patronymicName", patronymicName)
                .getResultList();
    }
}
