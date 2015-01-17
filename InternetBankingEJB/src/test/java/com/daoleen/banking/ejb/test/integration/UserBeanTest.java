package com.daoleen.banking.ejb.test.integration;

import com.daoleen.banking.beans.qualifiers.UserPasswordEncoder;
import com.daoleen.banking.domain.Client;
import com.daoleen.banking.domain.ClientAddress;
import com.daoleen.banking.domain.Identifiable;
import com.daoleen.banking.domain.User;
import com.daoleen.banking.exception.UserRegistrationException;
import com.daoleen.banking.repository.remote.ClientAddressRepositoryRemote;
import com.daoleen.banking.repository.remote.ClientRepositoryRemote;
import com.daoleen.banking.repository.remote.UserRepositoryRemote;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.ejb.EJB;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by alex on 12/17/14.
 */
public class UserBeanTest extends AbstractBeanTest {

    @EJB
    private UserRepositoryRemote userRepository;

    @EJB
    private ClientRepositoryRemote clientRepository;

    @EJB
    private ClientAddressRepositoryRemote clientAddressRepository;

    @Inject @UserPasswordEncoder
    private PasswordEncoder passwordEncoder;

    @Override
    protected UserRepositoryRemote getEntityRepository() {
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
    public void findByUsernamePassword() throws UserRegistrationException {
        ClientAddress address = clientAddressRepository.findAll().get(0);
        Client client = new Client("find", "byusername", "password", new Date(), "DD",
                332132134, new Date(), "11133311", address);
        client = clientRepository.save(client);
        userRepository.register("findbyusername", "andpassword", client);

        User user = userRepository.findByUsernamePassword("findbyusername", "andpassword");
        assertNotNull(user);
        assertTrue(passwordEncoder.matches("andpassword", user.getPassword()));
    }


    @Test
    public void testPasswordEncoder() {
        System.out.println("-- password encoder instance: " + passwordEncoder);
        String password = "mypassword";
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println("Encoded password is: " + encodedPassword);

        assertTrue(passwordEncoder.matches("mypassword", encodedPassword));
        assertFalse(passwordEncoder.matches("nmypassword", encodedPassword));
    }


    @Test
    public void userRegistrationTest() throws UserRegistrationException {
        ClientAddress address = clientAddressRepository.findAll().get(0);
        Client client = new Client("user", "registration", "test", new Date(), "DD",
                23123113, new Date(), "77733311", address);

        client = clientRepository.save(client);
        assertNotNull(client);

        User user = userRepository.register("userregistration", "test", client);
        assertNotNull(user);
        assertNotNull(user.getId());
    }


    @Test(expected = UserRegistrationException.class)
    public void userRegistrationExceptionTest() throws UserRegistrationException {
        try {
            userRepository.register("exceptionregistration", "test", null);
        }
        catch (UserRegistrationException e) {
            throw e;
        }
        finally {
            User exceptionregistrationUser = userRepository.findByUsername("exceptionregistration");
            assertNull(exceptionregistrationUser);
        }
    }
}
