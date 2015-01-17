package com.daoleen.banking.web.controller;

import com.daoleen.banking.domain.User;
import com.daoleen.banking.repository.remote.ClientRepositoryRemote;
import com.daoleen.banking.repository.remote.UserRepositoryRemote;
import com.daoleen.banking.web.infrastructure.ViewResult;
import com.daoleen.banking.web.infrastructure.annotations.Get;
import com.daoleen.banking.web.infrastructure.annotations.Post;
import com.daoleen.banking.web.infrastructure.annotations.Var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 * Created by alex on 1/17/15.
 */
@RequestScoped
public class AccountController extends AbstractController {
    private final static Logger logger = LoggerFactory.getLogger(AccountController.class);

    @EJB(lookup = "java:global/InternetBankingEJB/UserBean!com.daoleen.banking.repository.remote.UserRepositoryRemote")
    private UserRepositoryRemote userRepository;

    @EJB(lookup = "java:global/InternetBankingEJB/ClientBean!com.daoleen.banking.repository.remote.ClientRepositoryRemote")
    private ClientRepositoryRemote clientRepository;

    @Get
    public ViewResult signin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        logger.info("authentication is: " + authentication);
        return viewResult.setViewName("account/signin");
    }


    @Post
    public ViewResult signin(@Var("username") String username, @Var("password") String password) {
        User user = userRepository.findByUsernamePassword(username, password);
        logger.info("User instance found: {}", user);

        if (user == null) {
            return viewResult.add("system_notice", "Неверный логин либо пароль")
                    .setViewName("account/signin");
        }
        return null;
    }


    @Get
    public ViewResult clientRegistration() {
        return null;
    }
}
