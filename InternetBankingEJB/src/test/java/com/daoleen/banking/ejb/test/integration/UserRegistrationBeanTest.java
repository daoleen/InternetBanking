package com.daoleen.banking.ejb.test.integration;

import com.daoleen.banking.domain.User;
import com.daoleen.banking.exception.UserRegistrationException;
import com.daoleen.banking.repository.local.UserRegistrationRepository;
import org.junit.Test;

import javax.ejb.EJB;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by alex on 1/18/15.
 */
public class UserRegistrationBeanTest extends AbstractIntegrationTest {

    @EJB
    private UserRegistrationRepository userRegistrationRepository;

    @Test
    public void testUserRegistration() throws UserRegistrationException {
        String cityName = "Warsaw";
        //int cityId;

        String street = "Nipala";
        int houseNumber = 12;
        int housingNumber = 1;
        int apartmentNumber = 14;
        // City city

        String firstName = "Дятел";
        String lastName = "Андрей";
        String patronymicName = "Анатольевич";
        Date birthDate = new Date();
        String passportSeries = "MP";
        int passportNumber = 778811111;
        Date registrationDate = new Date();
        String mobileNumber = "77707378";
        // ClientAddress address


        String username = "dyatel";
        String password = "stukstuk";
//        boolean enabled;
//        boolean blocked;
//        String blockedReason;
        // Client client;

        userRegistrationRepository.registerCity(cityName);
        userRegistrationRepository.registerAddress(street, houseNumber, housingNumber, apartmentNumber);
        userRegistrationRepository.registerClient(firstName, lastName, patronymicName, birthDate, passportSeries,
                passportNumber, registrationDate, mobileNumber);
        User user = userRegistrationRepository.registerUser(username, password);

        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals(cityName, user.getClient().getAddress().getCity().getName());
    }

}
