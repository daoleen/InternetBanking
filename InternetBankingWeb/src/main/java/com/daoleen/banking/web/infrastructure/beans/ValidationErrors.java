package com.daoleen.banking.web.infrastructure.beans;

import javax.validation.ConstraintViolation;
import java.util.Map;
import java.util.Set;

/**
 * Created by alex on 1/18/15.
 */
public interface ValidationErrors {
    public String get(String field);

    public void setValidationErrors(Map<String, Set<ConstraintViolation<Object>>> validationErrors);

    public boolean hasErrors();
}
