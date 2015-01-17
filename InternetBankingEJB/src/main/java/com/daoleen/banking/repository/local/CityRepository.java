package com.daoleen.banking.repository.local;

import com.daoleen.banking.domain.City;
import com.daoleen.banking.repository.Repository;

import javax.ejb.Local;

/**
 * Created by alex on 1/17/15.
 */
@Local
public interface CityRepository extends Repository<City, Integer> {
    public City findByName(String name);
}
