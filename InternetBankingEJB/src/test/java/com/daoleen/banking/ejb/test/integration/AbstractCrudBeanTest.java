package com.daoleen.banking.ejb.test.integration;

import com.daoleen.banking.domain.BaseEntity;
import com.daoleen.banking.repository.local.CrudService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by alex on 12/10/14.
 */
public abstract class AbstractCrudBeanTest<B extends CrudService<BaseEntity<Serializable>, Serializable>>
        extends AbstractIntegrationTest
{
    private final static Logger logger = LoggerFactory.getLogger(AbstractCrudBeanTest.class);
    protected B crudService;
    protected abstract B getCrudService();
    protected abstract BaseEntity createNewEntity();

    @Before
    public void init() {
        crudService = getCrudService();
        assertNotNull(crudService);
    }

    @Before
    public void testCount() {
        List<BaseEntity<Serializable>> all = crudService.findAll();
        assertEquals(2, all.size());
    }

    @Test
    public void testInsert() {
        logger.error("LOGGER: testInsert runs");
        BaseEntity newEntity = createNewEntity();
        int initialSize = crudService.findAll().size();
        crudService.save(newEntity);
        int updatedSize = crudService.findAll().size();
        assertEquals(initialSize+1, updatedSize);
    }

    @Test
    public void testUpdate() {
        List<BaseEntity<Serializable>> all = crudService.findAll();
        int initialSize = all.size();
        BaseEntity<Serializable> entity = all.get(0);
        crudService.detach(entity);
        crudService.save(entity);   // count of entities must not change
        int updatedSize = crudService.findAll().size();
        assertEquals(initialSize, updatedSize);
    }

    @Test
    public void testDelete() {
        List<BaseEntity<Serializable>> all = crudService.findAll();
        int initialSize = all.size();
        System.out.println("initial size is: " + initialSize);
        BaseEntity<Serializable> entity = all.get(0);
        crudService.delete(entity);
        int updatedSize = crudService.findAll().size();
        System.out.println("updated size is: " + updatedSize);
        assertEquals(initialSize-1, updatedSize);
    }
}
