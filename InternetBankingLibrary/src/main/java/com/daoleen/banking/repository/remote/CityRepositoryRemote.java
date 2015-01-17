package com.daoleen.banking.repository.remote;

import com.daoleen.banking.domain.City;
import com.daoleen.banking.repository.Repository;

import javax.ejb.Remote;

/**
 * Created by alex on 12/10/14.
 */
@Remote
public interface CityRepositoryRemote extends Repository<City, Integer> {
    public City findByName(String name);
}
