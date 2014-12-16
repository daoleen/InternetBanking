package com.daoleen.banking.ejb.test.integration;

import com.daoleen.banking.domain.Identifiable;
import com.daoleen.banking.repository.local.Repository;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by alex on 12/10/14.
 */
public abstract class AbstractBeanTest<B extends Repository<Identifiable<Serializable>, Serializable>>
        extends AbstractIntegrationTest
{
    private final static Logger logger = LoggerFactory.getLogger(AbstractBeanTest.class);
    protected B repository;
    protected int recordsCount;

    protected abstract B getEntityRepository();

    protected abstract Identifiable createNewEntity();

    @Before
    public void init() {
        repository = getEntityRepository();
        assertNotNull(repository);
        List<Identifiable<Serializable>> all = repository.findAll();
        recordsCount = all.size();
    }

    @Test
    public void size() {
        assertTrue(recordsCount > 0);
    }

    @Test
    public void insert() {
        System.out.println("INSERT TEST");
        Identifiable newEntity = createNewEntity();
        System.out.println("-- initial size: " + recordsCount);
        repository.save(newEntity);
        int updatedSize = repository.findAll().size();
        System.out.println("-- updated size: " + updatedSize);
        assertEquals(recordsCount + 1, updatedSize);
        System.out.println("END INSERT TEST");
    }

    @Test
    public void update() {
        System.out.println("UPDATE TEST");
        List<Identifiable<Serializable>> all = repository.findAll();
        System.out.println("-- initial size: " + recordsCount);
        Identifiable<Serializable> entity = all.get(0);
        repository.detach(entity);
        repository.save(entity);   // count of entities must not change
        int updatedSize = repository.findAll().size();
        System.out.println("-- updated size: " + updatedSize);
        assertEquals(recordsCount, updatedSize);
        System.out.println("END UPDATE TEST");
    }

    @Test
    public void delete() {
        System.out.println("DELETE TEST");
        List<Identifiable<Serializable>> all = repository.findAll();
        System.out.println("initial size is: " + recordsCount);
        Identifiable<Serializable> entity = all.get(0);
        repository.delete(entity);
        int updatedSize = repository.findAll().size();
        System.out.println("updated size is: " + updatedSize);
        assertEquals(recordsCount - 1, updatedSize);
        System.out.println("END DELETE TEST");
    }

    @Test
    public void readById() {
        System.out.println("READ_BY_ID TEST");
        List<Identifiable<Serializable>> all = repository.findAll();
        Identifiable<Serializable> last = all.get(all.size() - 1);
        Identifiable<Serializable> found = repository.findById(last.getId());
        System.out.println("-- found: " + (found == null));
        assertNotNull(found);
        assertEquals(last.getId(), found.getId());
        System.out.println("END READ_BY_ID TEST");
    }
}
