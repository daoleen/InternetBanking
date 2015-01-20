package com.daoleen.banking.ejb;

import com.daoleen.banking.beans.qualifiers.UserPasswordEncoder;
import com.daoleen.banking.domain.Client;
import com.daoleen.banking.domain.User;
import com.daoleen.banking.exception.UserRegistrationException;
import com.daoleen.banking.repository.local.UserRepository;
import com.daoleen.banking.repository.remote.UserRepositoryRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.ejb.*;
import javax.enterprise.inject.Typed;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
@Stateless
@Local(UserRepository.class)
@Remote(UserRepositoryRemote.class)
@Typed(UserRepository.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserBean extends AbstractBean<User, Long> implements UserRepository, UserRepositoryRemote, Serializable {
    private final static Logger logger = LoggerFactory.getLogger(UserBean.class);
    private static final long serialVersionUID = 7799491018349450810L;

    @Inject @UserPasswordEncoder
    private PasswordEncoder passwordEncoder;


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
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public User register(String username, String password, Client client) throws UserRegistrationException {
        User user = new User();
        String hashedPassword = passwordEncoder.encode(password);

        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setBlocked(false);
        user.setEnabled(false);
        user.setClient(client);

        try {
            user = save(user);
            // TODO: sendEmailNotification();
        }
        catch (Exception e) {
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
        User user = findByUsername(username);

        if (user == null) {
            return null;
        }

        if(passwordEncoder.matches(plainPassword, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void disable(Long id) {
        em.createNamedQuery(User.DISABLE_USER)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
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
