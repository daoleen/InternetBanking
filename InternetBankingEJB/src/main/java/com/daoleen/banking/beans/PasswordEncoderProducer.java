package com.daoleen.banking.beans;

import com.daoleen.banking.beans.qualifiers.UserPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * Created by alex on 1/12/15.
 */
public class PasswordEncoderProducer {

    @ApplicationScoped
    @Produces @UserPasswordEncoder
    public PasswordEncoder producePasswordEncoder() {
        return new StandardPasswordEncoder();
    }

}
