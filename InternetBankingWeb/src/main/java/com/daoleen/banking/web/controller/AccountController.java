package com.daoleen.banking.web.controller;

import com.daoleen.banking.domain.City;
import com.daoleen.banking.domain.Client;
import com.daoleen.banking.domain.ClientAddress;
import com.daoleen.banking.domain.User;
import com.daoleen.banking.exception.UserRegistrationException;
import com.daoleen.banking.repository.remote.ClientRepositoryRemote;
import com.daoleen.banking.repository.remote.UserRegistrationRepositoryRemote;
import com.daoleen.banking.repository.remote.UserRepositoryRemote;
import com.daoleen.banking.web.infrastructure.BankUserAuthenticationToken;
import com.daoleen.banking.web.infrastructure.ViewResult;
import com.daoleen.banking.web.infrastructure.annotations.Get;
import com.daoleen.banking.web.infrastructure.annotations.Post;
import com.daoleen.banking.web.infrastructure.annotations.Validate;
import com.daoleen.banking.web.infrastructure.annotations.Var;
import com.daoleen.banking.web.infrastructure.domain.BankUser;
import com.daoleen.banking.web.viewModel.CityViewModel;
import com.daoleen.banking.web.viewModel.ClientAddressViewModel;
import com.daoleen.banking.web.viewModel.ClientViewModel;
import com.daoleen.banking.web.viewModel.UserViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Conversation;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by alex on 1/17/15.
 */
@ApplicationScoped
//@ConversationScoped // didn't work
public class AccountController extends AbstractController implements Serializable {
    private final static Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Inject
    private Conversation conversation;

    @EJB(lookup = "java:global/InternetBankingEJB/UserBean!com.daoleen.banking.repository.remote.UserRepositoryRemote")
    private UserRepositoryRemote userRepository;

    @EJB(lookup = "java:global/InternetBankingEJB/ClientBean!com.daoleen.banking.repository.remote.ClientRepositoryRemote")
    private ClientRepositoryRemote clientRepository;

    @EJB(lookup = "java:global/InternetBankingEJB/UserRegistrationBean!com.daoleen.banking.repository.remote.UserRegistrationRepositoryRemote")
    private UserRegistrationRepositoryRemote userRegistrationRepository;

    @Get
    public ViewResult signin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        logger.info("authentication is: " + authentication);
        return viewResult.setViewName("account/signin");
    }


    @Post
    public ViewResult signin(@Var("username") String username, @Var("password") String password) throws IOException {
        logger.info("username: {}, password: {}", username, password);
        User user = userRepository.findByUsernamePassword(username, password);
        logger.info("User instance found: {}", user);

        if (user == null) {
            return viewResult.add("system_notice", "Неверный логин либо пароль")
                    .setViewName("account/signin");
        }

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication auth = new BankUserAuthenticationToken(new BankUser(user));
        securityContext.setAuthentication(auth);
        response.sendRedirect("/InternetBankingWeb/app/ControlPanel/home");
        return viewResult.setViewName("error");
    }


    @Get
    public ViewResult register_city() {
        City city = userRegistrationRepository.getCity();
        if (conversation.isTransient()) {
            conversation.begin();
        }

        if (city == null) {
            city = new City();
        }

        return viewResult.add("city", city).setViewName("account/registerCity");
    }

    @Post
    public ViewResult register_city(@Validate @Var("city") CityViewModel city) throws IOException {
        if (validationErrors.hasErrors()) {
            return viewResult.add("city", city).setViewName("account/registerCity");
        }
        userRegistrationRepository.registerCity(city.getName());
        response.sendRedirect("/InternetBankingWeb/app/account/register_address");
        return viewResult.setViewName("error");
    }

    @Get
    public ViewResult register_address() {
        ClientAddress clientAddress = userRegistrationRepository.getClientAddress();
        ClientAddressViewModel clientAddressViewModel = new ClientAddressViewModel();

        if (clientAddress != null) {
            clientAddressViewModel.setApartmentNumber(Integer.toString(clientAddress.getApartmentNumber()));
            clientAddressViewModel.setHouseNumber(Integer.toString(clientAddress.getHouseNumber()));
            clientAddressViewModel.setHousingNumber(Integer.toString(clientAddress.getHousingNumber()));
            clientAddressViewModel.setStreet(clientAddress.getStreet());
        }

        return viewResult.add("address", clientAddressViewModel)
                .setViewName("account/registerClientAddress");
    }

    @Post
    public ViewResult register_address(@Validate @Var("clientAddress") ClientAddressViewModel clientAddress) throws IOException {

        logger.info("register_2 city: {}", userRegistrationRepository.getCity());

        if (validationErrors.hasErrors()) {
            return viewResult.add("address", clientAddress).setViewName("account/registerClientAddress");
        }

        userRegistrationRepository.registerAddress(clientAddress.getStreet(), clientAddress.getHouseNumber(),
                clientAddress.getHousingNumber(), clientAddress.getApartmentNumber());

        response.sendRedirect("/InternetBankingWeb/app/account/register_client");
        return viewResult.setViewName("error");
    }


    @Get
    public ViewResult register_client() {
        Client client = userRegistrationRepository.getClient();
        ClientViewModel clientViewModel = new ClientViewModel();

        if (client != null) {
            clientViewModel.setFirstName(client.getFirstName());
            clientViewModel.setLastName(client.getLastName());
            clientViewModel.setPatronymicName(client.getPatronymicName());
            clientViewModel.setMobileNumber(client.getMobileNumber());
            clientViewModel.setPassportNumber(Integer.toString(client.getPassportNumber()));
            clientViewModel.setPassportSeries(client.getPassportSeries());
        }

        return viewResult.add("client", clientViewModel)
                .setViewName("account/registerClient");
    }

    @Post
    public ViewResult register_client(@Validate @Var("client") ClientViewModel client) throws IOException {
        if (validationErrors.hasErrors()) {
            return viewResult.add("client", client).setViewName("account/registerClient");
        }

        userRegistrationRepository.registerClient(client.getFirstName(), client.getLastName(), client.getPatronymicName(),
                new Date(), client.getPassportSeries(), client.getPassportNumber(), new Date(), client.getMobileNumber());
        response.sendRedirect("/InternetBankingWeb/app/account/register_user");
        return viewResult.setViewName("error");
    }

    @Get
    public ViewResult register_user() {
        return viewResult.add("user", new UserViewModel())
                .setViewName("account/registerUser");
    }

    @Post
    public ViewResult register_user(@Validate @Var("user") UserViewModel user) throws IOException {
        if (validationErrors.hasErrors()) {
            return viewResult.add("user", user).setViewName("account/registerUser");
        }

        User registeredUser;
        try {
            registeredUser = userRegistrationRepository.registerUser(user.getUsername(), user.getPassword());
        } catch (UserRegistrationException e) {
            return viewResult.add("error", e.getMessage()).setViewName("error");
        }

        if (!conversation.isTransient()) {
            conversation.end();
        }

        response.sendRedirect("/InternetBankingWeb/app/account/register_success?username=" + registeredUser.getUsername());
        return viewResult.setViewName("error");
    }


    @Get
    public ViewResult register_success(@Var("username") String username) {
        return viewResult.add("username", username).setViewName("account/registerSuccess");
    }


    public UserRegistrationRepositoryRemote getUserRegistrationRepository() {
        return userRegistrationRepository;
    }

    public void setUserRegistrationRepository(UserRegistrationRepositoryRemote userRegistrationRepository) {
        this.userRegistrationRepository = userRegistrationRepository;
    }

    public static Logger getLogger() {
        return logger;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public UserRepositoryRemote getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepositoryRemote userRepository) {
        this.userRepository = userRepository;
    }

    public ClientRepositoryRemote getClientRepository() {
        return clientRepository;
    }

    public void setClientRepository(ClientRepositoryRemote clientRepository) {
        this.clientRepository = clientRepository;
    }
}
