package com.daoleen.banking.web.infrastructure;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * Created by alex on 1/16/15.
 */
public class ArgumentValidationException extends Exception {
    private final Set<ConstraintViolation<Object>> constraintViolations;

    public ArgumentValidationException(Set<ConstraintViolation<Object>> constraintViolations) {
        super("The validation exception was occurred");
        this.constraintViolations = constraintViolations;
    }

    public Set<ConstraintViolation<Object>> getConstraintViolations() {
        return constraintViolations;
    }
}
