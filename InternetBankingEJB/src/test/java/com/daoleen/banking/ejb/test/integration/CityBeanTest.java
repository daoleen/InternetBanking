package com.daoleen.banking.ejb.test.integration;

import com.daoleen.banking.domain.City;
import com.daoleen.banking.ejb.interfaces.local.CityRepository;
import org.junit.Before;
import org.junit.Test;

import javax.ejb.EJB;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by alex on 12/7/14.
 */
public class CityBeanTest extends AbstractIntegrationTest {

    @EJB
    private CityRepository cityRepository;

    @Test
    public void testIsLoaded() {
        assertNotNull(cityRepository);
    }

    @Before
    public void testCount() {
        List<City> all = cityRepository.findAll();
        assertEquals(2, all.size());
    }
}
