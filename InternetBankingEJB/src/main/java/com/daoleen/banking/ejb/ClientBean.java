package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.Client;
import com.daoleen.banking.domain.ClientAddress;
import com.daoleen.banking.exception.ClientRegistrationException;
import com.daoleen.banking.repository.local.ClientAddressRepository;
import com.daoleen.banking.repository.local.ClientRepository;
import com.daoleen.banking.repository.remote.ClientRepositoryRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.inject.Typed;
import javax.persistence.NoResultException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
@Stateless
@Local(ClientRepository.class)
@Remote(ClientRepositoryRemote.class)
@Typed(ClientRepository.class)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ClientBean extends AbstractBean<Client, Long> implements ClientRepository, ClientRepositoryRemote, Serializable {
    private final static Logger logger = LoggerFactory.getLogger(ClientBean.class);
    private static final long serialVersionUID = 7225596424434197075L;

    public ClientBean() {
        super(Client.class);
    }

    @Resource
    private SessionContext sessionContext;

    @EJB
    private ClientAddressRepository clientAddressRepository;


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Client register(Client client, ClientAddress address) throws ClientRegistrationException {
        try {
            ClientAddress clientAddress = clientAddressRepository.save(address);
            client.setAddress(clientAddress);
            client = save(client);
        }
        catch (Exception e) {
            sessionContext.setRollbackOnly();
            throw new ClientRegistrationException(e.getMessage());
        }
        return client;
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
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
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
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
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Client> findByAddressId(long addressId) {
        return em.createNamedQuery(Client.FIND_BY_ADDRESS_ID, Client.class)
                .setParameter("addressId", addressId)
                .getResultList();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Client> findbyAddress(String street, String city, int houseNumber, int apartmentNumber) {
        return em.createNamedQuery(Client.FIND_BY_ADDRESS, Client.class)
                .setParameter("city", city)
                .setParameter("street", street)
                .setParameter("houseNumber", houseNumber)
                .setParameter("apartmentNumber", apartmentNumber)
                .getResultList();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Client> findByFIO(String firstName, String lastName, String patronymicName) {
        return em.createNamedQuery(Client.FIND_BY_FIO, Client.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .setParameter("patronymicName", patronymicName)
                .getResultList();
    }
}
