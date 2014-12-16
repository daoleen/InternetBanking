package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.BaseEntity;
import com.daoleen.banking.repository.local.CrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
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
@TransactionManagement(TransactionManagementType.CONTAINER)
public abstract class AbstractCrudBean<T extends BaseEntity<I>, I extends Serializable>
        implements CrudService<T, I>
{
    private final static Logger logger = LoggerFactory.getLogger(AbstractCrudBean.class);
    private Class<T> entityClass;

    @PersistenceContext
    protected EntityManager em;

//    @Inject
//    protected TransactionManager tx;

//    @Resource
//    protected UserTransaction tx;

    public AbstractCrudBean(Class<T> clazz) {
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
    public void save(T entity) {
        if(entity.getIdentifier() != null) {
            logger.debug("Updating the existing entity");
            em.merge(entity);
        } else {
            logger.debug("Persist new entity");
            em.persist(entity);
        }
    }

    @Override
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(T entity) {

//        try {
//            tx.begin();
//        } catch (NotSupportedException e) {
//            logger.error("Transactions is not supported");
//            e.printStackTrace();
//        } catch (SystemException e) {
//            logger.error("An exception was occurred while transaction starting");
//            e.printStackTrace();
//        }

        logger.debug("Removing entity from database");

        em.remove(em.merge(entity));

//        em.joinTransaction();
//        try {
//            //em.remove(entity);
//            String query = String.format("delete c from {} c where id=1", entityClass.getName());
//            em.createQuery(query).executeUpdate();
//        }
//        catch (IllegalArgumentException e) {
//            logger.debug("Couldn't delete the entity. Probably it was detached. Trying to attach and delete...");
//            try {
//                em.merge(entity);
//                em.remove(entity);
//            }
//            catch (IllegalArgumentException illex) {
//                logger.error("Sorry, i can't delete the entity. Given entity is not an entity or it was removed");
//                logger.error(illex.getStackTrace().toString());
//                try {
//                    tx.rollback();
//                } catch (SystemException e1) {
//                    logger.error("An exception was occurred while transaction rollbacks");
//                    e1.printStackTrace();
//                }
//            }
//        }


//        try {
//            tx.commit();
//        } catch (RollbackException e) {
//            e.printStackTrace();
//        } catch (HeuristicMixedException e) {
//            e.printStackTrace();
//        } catch (HeuristicRollbackException e) {
//            e.printStackTrace();
//        } catch (SystemException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void detach(T entity) {
        logger.debug("Detaching entity from PersistenceContext");
        em.detach(entity);
    }
}
