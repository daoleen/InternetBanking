package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.Client;
import com.daoleen.banking.domain.User;
import com.daoleen.banking.exception.UserRegistrationException;
import com.daoleen.banking.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ejb.*;
import javax.enterprise.inject.Typed;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
@Stateless
@Local(UserRepository.class)
@Typed(UserRepository.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserBean extends AbstractBean<User, Long> implements UserRepository {
    private final static Logger logger = LoggerFactory.getLogger(UserBean.class);

    @Inject
    private SessionContext sessionContext;

    public UserBean() {
        super(User.class);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<User> findAll() {
        return em.createNamedQuery(User.FIND_ALL, User.class).getResultList();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public User findByUsername(String username) {
        try {
            return em.createNamedQuery(User.FIND_BY_USERNAME, User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException ex) {
            logger.info("User [{}] has not been found.", username);
        }
        return null;
    }

    @Override
    public User register(String username, String password, Client client) throws UserRegistrationException {
        User user = new User();
        user.setUsername(username);
        //TODO: user.setPassword(hashedPassword);
        user.setBlocked(false);
        user.setEnabled(false);
        user.setClient(client);

        try {
            user = save(user);
            // TODO: sendEmailNotification();
        }
        catch (Exception e) {
            sessionContext.setRollbackOnly();
            throw new UserRegistrationException(e.getMessage());
        }
        return user;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<User> findBlockedUsers() {
        return em.createNamedQuery(User.FIND_BLOCKED, User.class)
                .getResultList();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<User> findDisabledUsers() {
        return em.createNamedQuery(User.FIND_DISABLED, User.class)
                .getResultList();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public User findByUsernamePassword(String username, String plainPassword) {
        throw new NotImplementedException();

//        String hashedPassword = "EXECUTING HASH GENERATOR BEAN";
//
//        try {
//            return em.createNamedQuery("findByUsernamePassword", User.class)
//                    .setParameter("username", username)
//                    .setParameter("hashedPass", hashedPassword)
//                    .getSingleResult();
//        }
//        catch (NoResultException e) {
//            logger.info("User {} with specified password has not been found.", username);
//        }
//        return null;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void disable(Long id) {
        em.createNamedQuery(User.DISABLE_USER)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void block(Long id, String reason) {
        try {
            em.createNamedQuery(User.BLOCK_USER)
                    .setParameter("id", id)
                    .setParameter("reason", reason)
                    .executeUpdate();
        }
        catch (Exception e) {
            System.out.println("exception while blocking user: " + e.getStackTrace().toString());
            logger.error(e.getStackTrace().toString());
        }
    }
}
