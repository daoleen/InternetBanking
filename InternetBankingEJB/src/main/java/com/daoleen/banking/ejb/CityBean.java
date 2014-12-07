package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.City;
import com.daoleen.banking.ejb.interfaces.local.CityRepository;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by alex on 12/6/14.
 */
@Stateless
@Local(CityRepository.class)
public class CityBean implements CityRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<City> findAll() {
        return em.createQuery("select c from City c")
                .getResultList();
    }
}
