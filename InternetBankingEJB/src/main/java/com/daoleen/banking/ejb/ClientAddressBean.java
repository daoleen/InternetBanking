package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.ClientAddress;
import com.daoleen.banking.repository.local.ClientAddressRepository;
import com.daoleen.banking.repository.remote.ClientAddressRepositoryRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.enterprise.inject.Typed;
import javax.persistence.NoResultException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
@Stateless
@Local(ClientAddressRepository.class)
@Remote(ClientAddressRepositoryRemote.class)
@Typed(ClientAddressRepository.class)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ClientAddressBean extends AbstractBean<ClientAddress, Long>
        implements ClientAddressRepository, ClientAddressRepositoryRemote, Serializable {
    private final static Logger logger = LoggerFactory.getLogger(ClientAddressBean.class);
    private static final long serialVersionUID = -286074788866278518L;

    public ClientAddressBean() {
        super(ClientAddress.class);
    }


    @Override
    public List<ClientAddress> findByAddress(String street, int cityId,
                                             int houseNumber, int apartmentNumber) {
        try {
            return em.createNamedQuery(ClientAddress.FIND_BY_ADDRESS, ClientAddress.class)
                    .setParameter("cityId", cityId)
                    .setParameter("street", street)
                    .setParameter("houseNumber", houseNumber)
                    .setParameter("apartmentNumber", apartmentNumber)
                    .getResultList();
        } catch (NoResultException ex) {
            logger.info("ClientAddress has not been found.");
        }
        return new ArrayList<ClientAddress>();
    }

    @Override
    public List<ClientAddress> findByAddress(String street, String cityName,
                                             int houseNumber, int apartmentNumber) {
        try {
            return em.createNamedQuery(ClientAddress.FIND_BY_ADDRESS_CITY_NAME, ClientAddress.class)
                    .setParameter("cityName", cityName)
                    .setParameter("street", street)
                    .setParameter("houseNumber", houseNumber)
                    .setParameter("apartmentNumber", apartmentNumber)
                    .getResultList();
        } catch (NoResultException ex) {
            logger.info("ClientAddress has not been found.");
        }
        return new ArrayList<ClientAddress>();
    }
}
