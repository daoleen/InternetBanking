package com.daoleen.banking.web.infrastructure.beans.impl;

import com.daoleen.banking.web.infrastructure.ArgumentValidationException;
import com.daoleen.banking.web.infrastructure.InitializationControllerException;
import com.daoleen.banking.web.infrastructure.annotations.Validate;
import com.daoleen.banking.web.infrastructure.annotations.Var;
import com.daoleen.banking.web.infrastructure.beans.ParametersBinder;
import com.daoleen.banking.web.infrastructure.beans.PrimitiveFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * Created by alex on 1/16/15.
 */
@Singleton
public class ParametersBinderImpl implements ParametersBinder {
    private final static Logger logger = LoggerFactory.getLogger(ParametersBinderImpl.class);
    private final static String PARAM_VALIDATION_ERROR_KEY = "field";

    @Inject
    private Validator validator;

    @Inject
    private PrimitiveFactory primitiveFactory;

    @Override
    public List<Object> bind(Parameter[] parameters, HttpServletRequest request) throws InitializationControllerException, ArgumentValidationException {
        List<Object> params = new ArrayList<>();
        Map<String, Set<ConstraintViolation<Object>>> validationErrors = new HashMap<>();

        for (Parameter p : parameters) {
            String parameterName = p.getAnnotation(Var.class).value();
            Object parameterObject;

            if (!p.getType().isPrimitive() && !p.getType().equals(String.class)) {
                // perform binding
                parameterObject = bind(p, request);

                // perform validation if @Validate present
                if (p.isAnnotationPresent(Validate.class)) {
                    try {
                        validateParameter(parameterObject);
                    } catch (ArgumentValidationException e) {
                        logger.info("The constratint violations was found while validating parameter: {}", parameterName);
                        validationErrors.put(parameterName, e.getValidationErrors().get(PARAM_VALIDATION_ERROR_KEY));
                    }
                }
            } else {
                parameterObject = primitiveFactory.getPrimitive(p.getType().getName(), request.getParameter(parameterName));
                if (parameterObject == null) {
                    throw new InitializationControllerException("Can not find parameter with name: " + parameterName);
                }
            }

            // add parameterObject to ordered set
            params.add(parameterObject);
        }

        if (!validationErrors.isEmpty()) {
            throw new ArgumentValidationException(validationErrors, params);
        }

        return params;
    }

    @Override
    public Object bind(Parameter p, HttpServletRequest request) throws InitializationControllerException {
        Object parameterObject = createParameterObjectInstance(p);
        Field[] parameterObjectFields = p.getType().getDeclaredFields();

        for (Field f : parameterObjectFields) {
            String fieldName = f.getName();
            String fieldValue = request.getParameter(fieldName);

            if (fieldValue == null) {
                throw new InitializationControllerException("Can not find parameter with name " + fieldName
                        + " to instantiating a new object of type " + p.getType().getName());
            }

            try {
                String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                getMethodFromObject(parameterObject, methodName).invoke(parameterObject, fieldValue);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                String message = String.format("An IllegalAccessException was occurred while trying to set value to field of object. Exception: %s. " +
                                "Parameter type: %s, parameter name: %s",
                        e.getMessage(), p.getType().getName(), fieldName
                );
                logger.error(message);
                throw new InitializationControllerException(message);
            }
        }
        return parameterObject;
    }

    @Override
    public void validateParameter(Object parameterObject) throws ArgumentValidationException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(parameterObject);
        if (!constraintViolations.isEmpty()) {
            Map<String, Set<ConstraintViolation<Object>>> validationErrors = new HashMap<>(1);
            validationErrors.put(PARAM_VALIDATION_ERROR_KEY, constraintViolations);
            throw new ArgumentValidationException(validationErrors);
        }
    }


    private Object createParameterObjectInstance(Parameter p) throws InitializationControllerException {
        Object parameterObject;
        try {
            parameterObject = p.getType().newInstance();
        } catch (InstantiationException e) {
            String message = String.format("An InstantiationException was occurred while trying to set value to field of object. Exception: %s. " +
                            "Parameter type: %s",
                    e.getMessage(), p.getType().getName()
            );
            logger.error(message);
            throw new InitializationControllerException(message);
        } catch (IllegalAccessException e) {
            String message = String.format("An IllegalAccessException was occurred while trying to set value to field of object. Exception: %s. " +
                            "Parameter type: %s",
                    e.getMessage(), p.getType().getName()
            );
            logger.error(message);
            throw new InitializationControllerException(message);
        }
        return parameterObject;
    }


    private Method getMethodFromObject(Object o, String name) throws NoSuchMethodException {
        Method[] methods = o.getClass().getMethods();
        for (Method m : methods) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        throw new NoSuchMethodException("Su such method: " + name);
    }
}
