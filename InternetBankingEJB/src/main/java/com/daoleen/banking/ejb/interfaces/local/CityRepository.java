package com.daoleen.banking.ejb.interfaces.local;

import com.daoleen.banking.domain.City;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by alex on 12/6/14.
 */
@Local
public interface CityRepository {
    public List<City> findAll();
}
