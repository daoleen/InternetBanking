package com.daoleen.banking.web.infrastructure.beans;

import com.daoleen.banking.web.infrastructure.ArgumentValidationException;
import com.daoleen.banking.web.infrastructure.InitializationControllerException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * Created by alex on 1/16/15.
 */
public interface ParametersBinder {
    public List<Object> bind(Parameter[] parameters, HttpServletRequest request) throws InitializationControllerException, ArgumentValidationException;

    public Object bind(Parameter p, HttpServletRequest request) throws InitializationControllerException;

    public void validateParameter(Object parameterObject) throws ArgumentValidationException;
}
