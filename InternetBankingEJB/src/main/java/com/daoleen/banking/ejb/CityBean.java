package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.City;
import com.daoleen.banking.repository.local.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityNotFoundException;

/**
 * Created by alex on 12/6/14.
 */
@Stateless
@Local(CityService.class)
public class CityBean extends AbstractCrudBean<City, Integer> implements CityService {
    private static final Logger logger = LoggerFactory.getLogger(CityBean.class);

    public CityBean() {
        super(City.class);
    }

    @Override
    public City findByName(String name) {
        logger.debug("Finding city by name: {}", name);
        try {
            return (City) em.createNamedQuery("findByName")
                    .setParameter("name", name)
                    .getSingleResult();
        }
        catch (EntityNotFoundException e) {
            logger.debug("City has not been found");
        }
        return null;
    }
}
