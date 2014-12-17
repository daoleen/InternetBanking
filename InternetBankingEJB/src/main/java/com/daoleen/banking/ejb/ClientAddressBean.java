package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.ClientAddress;
import com.daoleen.banking.repository.local.ClientAddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.inject.Typed;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
@Stateless
@Local(ClientAddressRepository.class)
@Typed(ClientAddressRepository.class)
public class ClientAddressBean extends AbstractBean<ClientAddress, Long>
        implements ClientAddressRepository {
    private final static Logger logger = LoggerFactory.getLogger(ClientAddressBean.class);

    public ClientAddressBean() {
        super(ClientAddress.class);
    }

    @Override
    public List<ClientAddress> findByAddress(String street, int cityId,
                                             int houseNumber, int apartmentNumber) {
        try {
            return em.createNamedQuery("findByAddress", ClientAddress.class)
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
}
