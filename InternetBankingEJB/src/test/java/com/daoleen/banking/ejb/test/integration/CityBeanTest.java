package com.daoleen.banking.ejb.test.integration;

import com.daoleen.banking.domain.City;
import com.daoleen.banking.domain.Identifiable;
import com.daoleen.banking.repository.CityRepository;
import org.junit.Test;

import javax.ejb.EJB;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by alex on 12/7/14.
 */
public class CityBeanTest extends AbstractBeanTest {

    @EJB
    private CityRepository cityRepository;

    @Override
    protected CityRepository getEntityRepository() {
        return cityRepository;
    }

    @Override
    protected Identifiable createNewEntity() {
        return new City("Kiev");
    }


    @Test
    public void findByName() {
        System.out.println("FIND_BY_NAME TEST");
        City smolensk = cityRepository.findByName("Minsk");
        System.out.println("Found: " + smolensk);
        assertNotNull(smolensk);
        System.out.println("END FIND_BY_NAME TEST");
    }

    @Test
    public void findNotFound() {
        City notSuchCity = cityRepository.findByName("notSuchCity");
        assertNull(notSuchCity);
    }
}
