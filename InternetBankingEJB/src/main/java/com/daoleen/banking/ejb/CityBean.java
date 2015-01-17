package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.City;
import com.daoleen.banking.repository.local.CityRepository;
import com.daoleen.banking.repository.remote.CityRepositoryRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.enterprise.inject.Typed;
import javax.persistence.NoResultException;

/**
 * Created by alex on 12/6/14.
 */
@Stateless
@Local(CityRepository.class)
@Remote(CityRepositoryRemote.class)
@Typed(CityRepository.class)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CityBean extends AbstractBean<City, Integer> implements CityRepository, CityRepositoryRemote {
    private static final Logger logger = LoggerFactory.getLogger(CityBean.class);

    public CityBean() {
        super(City.class);
    }

    @Override
    public City findByName(String name) {
        logger.debug("Finding city by name: {}", name);
        try {
            return em.createNamedQuery(City.FIND_BY_NAME, City.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            logger.info("City has not been found");
        }
        return null;
    }
}
