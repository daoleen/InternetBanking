package com.daoleen.banking.web.infrastructure.beans.impl;

import com.daoleen.banking.domain.User;
import com.daoleen.banking.repository.remote.UserRepositoryRemote;
import com.daoleen.banking.web.infrastructure.domain.BankUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.ejb.EJB;
import javax.inject.Singleton;

/**
 * Created by alex on 1/17/15.
 */
@Singleton
public class BankingUserDetailsService implements UserDetailsService {

    @EJB(lookup = "java:global/InternetBankingEJB/UserBean!com.daoleen.banking.repository.remote.UserRepositoryRemote")
    private UserRepositoryRemote userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            //noinspection deprecation
            throw new UsernameNotFoundException("User '%s' could not be found", username);
        }

        return new BankUser(user);
    }
}
