package com.daoleen.banking.ejb.test.integration;

import com.daoleen.banking.domain.Client;
import com.daoleen.banking.domain.Identifiable;
import com.daoleen.banking.domain.User;
import com.daoleen.banking.repository.local.ClientRepository;
import com.daoleen.banking.repository.local.UserRepository;
import org.junit.Test;

import javax.ejb.EJB;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by alex on 12/17/14.
 */
public class UserBeanTest extends AbstractBeanTest {

    @EJB
    private UserRepository userRepository;

    @EJB
    private ClientRepository clientRepository;

    @Override
    protected UserRepository getEntityRepository() {
        return userRepository;
    }

    @Override
    protected Identifiable createNewEntity() {
        Client client = clientRepository.findAll().get(0);
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setClient(client);
        return user;
    }

    @Test
    public void findByUsernameRegistryDependent() {
        User user = userRepository.findByUsername("alexssource@gmail.com");
        assertNotNull(user);
    }

    @Test
    public void findByUsernameRegistryIndependent() {
        User user = userRepository.findByUsername("aleXssource@gmail.com");
        assertNull(user);
    }

    @Test
    public void findByUsernameNotFound() {
        User user = userRepository.findByUsername("not-found-user");
        assertNull(user);
    }
}
