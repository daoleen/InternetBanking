package com.daoleen.banking.ejb.test.integration;

import com.daoleen.banking.domain.Client;
import com.daoleen.banking.domain.Identifiable;
import com.daoleen.banking.domain.User;
import com.daoleen.banking.repository.ClientRepository;
import com.daoleen.banking.repository.UserRepository;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ejb.EJB;
import java.util.List;

import static org.junit.Assert.*;

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

    @Test
    public void blockUser() {
        User user = userRepository.findByUsername("alexssource@gmail.com");
        userRepository.block(user.getId(), "bad user");
        List<User> blockedUsers = userRepository.findBlockedUsers();

        assertNotNull(blockedUsers);
        assertTrue(blockedUsers.size() > 0);

        assertTrue(blockedUsers.contains(user));
    }

    @Test
    public void findBlocked() {
        int initialSize = userRepository.findBlockedUsers().size();
        User user = userRepository.findAll().get(0);
        userRepository.block(user.getId(), "i want so");
        int resultSize = userRepository.findBlockedUsers().size();
        assertEquals(initialSize+1, resultSize);
    }

    @Test
    public void disabledUsers() {
        int initialSize = userRepository.findDisabledUsers().size();
        User user = userRepository.findAll().get(0);
        userRepository.disable(user.getId());
        int resultSize = userRepository.findDisabledUsers().size();
        assertEquals(initialSize+1, resultSize);
    }


    @Test
    public void findByUsernamePassword() {
        //TODO: "Before I need to create registration for users"
        throw new NotImplementedException();
    }
}
