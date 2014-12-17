package com.daoleen.banking.repository;

import com.daoleen.banking.domain.User;

import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
public interface UserRepository extends Repository<User, Long> {

    /**
     * Retrieve a list of ACTIVE (non-blocked) users
     * @return list of active users
     */
    @Override
    public List<User> findAll();

    public User findByUsername(String username);
}
