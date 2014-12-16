package com.daoleen.banking.ejb.test.integration;

import com.daoleen.banking.domain.BaseEntity;
import com.daoleen.banking.domain.City;
import com.daoleen.banking.repository.local.CityService;

import javax.ejb.EJB;

/**
 * Created by alex on 12/7/14.
 */
public class CityBeanTest extends AbstractCrudBeanTest {

    @EJB
    private CityService cityService;

    @Override
    protected CityService getCrudService() {
        return cityService;
    }

    @Override
    protected BaseEntity createNewEntity() {
        return new City("Kiev");
    }
}
