package com.daoleen.banking.repository.local;

import com.daoleen.banking.domain.City;

/**
 * Created by alex on 12/10/14.
 */
public interface CityService extends CrudService<City, Integer> {
    public City findByName(String name);
}
