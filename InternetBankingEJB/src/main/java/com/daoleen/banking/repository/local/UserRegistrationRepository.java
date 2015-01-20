package com.daoleen.banking.repository.local;

import com.daoleen.banking.domain.City;
import com.daoleen.banking.domain.Client;
import com.daoleen.banking.domain.ClientAddress;
import com.daoleen.banking.domain.User;
import com.daoleen.banking.exception.UserRegistrationException;

import javax.ejb.Local;
import java.util.Date;

/**
 * Created by alex on 1/18/15.
 */
@Local
public interface UserRegistrationRepository {
    public void registerCity(String name);

    public void registerAddress(String street, int houseNumber, int housingNumber, int apartmentNumber);

    public void registerClient(String firstName, String lastName, String patronymicName, Date birthDate, String passportSeries,
                               int passportNumber, Date registrationDate, String mobileNumber);

    public User registerUser(String username, String password) throws UserRegistrationException;

    public City getCity();

    public ClientAddress getClientAddress();

    public Client getClient();

    public User getUser();
}
