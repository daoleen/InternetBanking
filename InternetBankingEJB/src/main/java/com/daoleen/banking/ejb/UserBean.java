package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.User;
import com.daoleen.banking.repository.local.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.inject.Typed;
import javax.persistence.NoResultException;

/**
 * Created by alex on 12/17/14.
 */
@Stateless
@Local(UserRepository.class)
@Typed(UserRepository.class)
public class UserBean extends AbstractBean<User, Long> implements UserRepository {
    private final static Logger logger = LoggerFactory.getLogger(UserBean.class);

    public UserBean() {
        super(User.class);
    }

    @Override
    public User findByUsername(String username) {
        try {
            return em.createNamedQuery("findByUsername", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException ex) {
            logger.info("User [{}] has not been found.", username);
        }
        return null;
    }
}
