package com.daoleen.banking.repository.local;

import com.daoleen.banking.domain.Identifiable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by alex on 12/10/14.
 */

/**
 * Represents an abstract repository interface,
 * that has a standard crud operations.
 * @type T : the entity object
 * @type I : type of id field
 */
public interface Repository<T extends Identifiable<I>, I extends Serializable> {
    public List<T> findAll();
    public T findById(I id);
    public void save(T entity);
    public void delete(T entity);
    public void detach(T entity);
}
