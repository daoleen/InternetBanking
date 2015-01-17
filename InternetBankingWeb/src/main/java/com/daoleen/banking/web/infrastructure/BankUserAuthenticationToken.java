package com.daoleen.banking.web.infrastructure;

import com.daoleen.banking.web.infrastructure.domain.BankUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * Created by alex on 1/17/15.
 */
public class BankUserAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private Long id;

    public BankUserAuthenticationToken(BankUser user) {
        super(user, user.getPassword(), user.getAuthorities());
        setId(user.getUser().getId());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
