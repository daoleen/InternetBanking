package com.daoleen.banking.ejb;

import com.daoleen.banking.ejb.interfaces.TestBeanInterface;

import javax.ejb.Local;
import javax.ejb.Stateless;

/**
 * Created by Alex Kozlov on 04.12.2014.
 */
@Stateless
@Local(TestBeanInterface.class)
public class TestBean implements TestBeanInterface {
    @Override
    public String testMethod() {
        return "InternetBanking";
    }
}
