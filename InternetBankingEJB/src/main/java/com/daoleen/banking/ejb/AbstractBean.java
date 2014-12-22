package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.Identifiable;
import com.daoleen.banking.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

/**
 * Created by alex on 12/10/14.
 */

/**
 * Represents an abstract service repository bean,
 * that has a standard crud operations.
 * @type T : the entity object
 * @type I : type of id field
 */
@Stateless
public abstract class AbstractBean<T extends Identifiable<I>, I extends Serializable>
        implements Repository<T, I>
{
    private final static Logger logger = LoggerFactory.getLogger(AbstractBean.class);
    @PersistenceContext
    protected EntityManager em;
    private Class<T> entityClass;

    public AbstractBean(Class<T> clazz) {
        entityClass = clazz;
    }

    @Override
    public List<T> findAll() {
        String query = String.format("select e from %s e", entityClass.getName());
        List<T> entities = em.createQuery(query).getResultList();
        logger.debug("An {} entities was found by findAll() method", entities.size());
        return entities;
        //return em.createNamedQuery("findAll").getResultList();
    }

    @Override
    public T findById(I id) {
        logger.debug("Finding entity by id<%s>: %s", entityClass.getName(), id.toString());
        return em.find(entityClass, id);
    }

    @Override
    public T save(T entity) {
        em.persist(entity = em.merge(entity));
        return entity;
    }

    @Override
    public void delete(T entity) {
        logger.debug("Removing entity from database");
        em.remove(em.merge(entity));
    }

    @Override
    public void detach(T entity) {
        logger.debug("Detaching entity from PersistenceContext");
        em.detach(entity);
    }
}
