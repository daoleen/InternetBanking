package com.daoleen.banking.web.infrastructure;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by alex on 1/16/15.
 */
public class ArgumentValidationException extends Exception {
    private final Map<String, Set<ConstraintViolation<Object>>> validationErrors;
    private final List<Object> params;

    public ArgumentValidationException(Map<String, Set<ConstraintViolation<Object>>> validationErrors) {
        this(validationErrors, null);
    }

    public ArgumentValidationException(Map<String, Set<ConstraintViolation<Object>>> validationErrors, List<Object> params) {
        super("The validation exception was occurred");
        this.validationErrors = validationErrors;
        this.params = params;
    }

    public final Map<String, Set<ConstraintViolation<Object>>> getValidationErrors() {
        return validationErrors;
    }

    public final List<Object> getParams() {
        return params;
    }
}
