package com.daoleen.banking.repository.local;

import com.daoleen.banking.domain.Client;
import com.daoleen.banking.domain.User;
import com.daoleen.banking.exception.UserRegistrationException;
import com.daoleen.banking.repository.Repository;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by alex on 1/17/15.
 */
@Local
public interface UserRepository extends Repository<User, Long> {
    public User findByUsername(String username);
    public User register(String username, String password, Client client) throws UserRegistrationException;
    public List<User> findBlockedUsers();
    public List<User> findDisabledUsers();
    public User findByUsernamePassword(String username, String plainPassword);
    public void disable(Long id);
    public void block(Long id, String reason);
}
