package com.daoleen.banking.web.infrastructure;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Created by alex on 1/17/15.
 */
public class FioTest {
    @NotNull
    @Length(min = 3, max = 6)
    private String firstName;

    @Length(min = 3, max = 6)
    private String lastName;

    @Length(min = 3, max = 6)
    private String patrName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatrName() {
        return patrName;
    }

    public void setPatrName(String patrName) {
        this.patrName = patrName;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", lastName, firstName, patrName);
    }
}