package com.daoleen.banking.ejb;

import com.daoleen.banking.domain.City;
import com.daoleen.banking.domain.Client;
import com.daoleen.banking.domain.ClientAddress;
import com.daoleen.banking.domain.User;
import com.daoleen.banking.exception.UserRegistrationException;
import com.daoleen.banking.repository.local.*;
import com.daoleen.banking.repository.remote.UserRegistrationRepositoryRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.inject.Typed;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 1/18/15.
 */
@Stateful
@Local(UserRegistrationRepository.class)
@Remote(UserRegistrationRepositoryRemote.class)
@Typed(UserRegistrationRepository.class)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class UserRegistrationBean implements UserRegistrationRepository, UserRegistrationRepositoryRemote, Serializable {
    private final static Logger logger = LoggerFactory.getLogger(UserRegistrationBean.class);
    private static final long serialVersionUID = -6475155234126246374L;

    @EJB
    private CityRepository cityRepository;

    @EJB
    private ClientAddressRepository clientAddressRepository;

    @EJB
    private ClientRepository clientRepository;

    @EJB
    private UserRepository userRepository;

    @Resource
    private SessionContext sessionContext;

    private City city;
    private ClientAddress clientAddress;
    private Client client;
    private User user;

    @Override
    public void registerCity(String name) {
        logger.info("UserRegistrationBean.registerCity: cityName is: {}", name);
        city = cityRepository.findByName(name);
        if (city == null) {
            city = new City(name);
        }
    }

    @Override
    public void registerAddress(String street, int houseNumber, int housingNumber, int apartmentNumber) {
        List<ClientAddress> address = clientAddressRepository.findByAddress(street, city.getName(), houseNumber, apartmentNumber);
        logger.info("UserRegistrationBean.registerAddress: cityName is: {}", city.getName());

        if (address.size() > 0) {
            clientAddress = address.get(0);
        } else {
            clientAddress = new ClientAddress();
            clientAddress.setStreet(street);
            clientAddress.setHouseNumber(houseNumber);
            clientAddress.setHousingNumber(housingNumber);
            clientAddress.setApartmentNumber(apartmentNumber);
        }
    }

    @Override
    public void registerClient(String firstName, String lastName, String patronymicName, Date birthDate, String passportSeries,
                               int passportNumber, Date registrationDate, String mobileNumber) {
        logger.info("UserRegistrationBean.registerClient: cityName is: {}, street is: {}", city.getName(), clientAddress.getStreet());
        client = clientRepository.findByPassport(passportSeries, passportNumber);

        if (client == null) {
            client = new Client();
            client.setFirstName(firstName);
            client.setLastName(lastName);
            client.setPatronymicName(patronymicName);
            client.setBirthDate(birthDate);
            client.setPassportSeries(passportSeries);
            client.setPassportNumber(passportNumber);
            client.setRegistrationDate(registrationDate);
            client.setMobileNumber(mobileNumber);
        }
    }

    @Override
    @Remove
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public User registerUser(String username, String password) throws UserRegistrationException {
        User foundUser = userRepository.findByUsername(username);

        if (foundUser != null) {
            throw new UserRegistrationException("Такой юзер уже существует");
        }

        try {
            if (city.getId() == null) {
                city = cityRepository.save(city);
            }

            if (clientAddress.getId() == null) {
                clientAddress.setCity(city);
                clientAddress = clientAddressRepository.save(clientAddress);
            }

            if (client.getId() == null) {
                client.setAddress(clientAddress);
                client = clientRepository.save(client);
            }

            user = userRepository.register(username, password, client);
        } catch (Exception e) {
            sessionContext.setRollbackOnly();
            logger.error(e.getMessage());
            throw new UserRegistrationException(e.getMessage());
        }
        return user;
    }
}
