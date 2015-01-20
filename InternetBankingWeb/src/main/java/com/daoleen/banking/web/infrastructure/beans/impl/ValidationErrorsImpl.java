package com.daoleen.banking.web.infrastructure.beans.impl;

import com.daoleen.banking.web.infrastructure.beans.ValidationErrors;

import javax.enterprise.context.RequestScoped;
import javax.validation.ConstraintViolation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by alex on 1/18/15.
 */
@RequestScoped
public class ValidationErrorsImpl implements ValidationErrors {
    private Map<String, String> validationErrors = new HashMap<>();

    /**
     * Returns error for field or empty string if there are no errors for that field
     * This method NEVER return null!
     *
     * @param field
     * @return validation error for field
     */
    @Override
    public String get(String field) {
        String res = validationErrors.get(field);
        return res != null ? res : "";
    }

    /**
     * Set ups the bean
     *
     * @param validationErrors map of Hibernate validator constraint violations
     */
    @Override
    public void setValidationErrors(Map<String, Set<ConstraintViolation<Object>>> validationErrors) {
        if (validationErrors != null && !validationErrors.isEmpty()) {
            validationErrors.forEach((k, v) -> {
                this.validationErrors.put(k, v.stream().map(val -> val.getMessage())
                        .collect(Collectors.joining("<br>")));
            });
        }
    }

    @Override
    public boolean hasErrors() {
        return !validationErrors.isEmpty();
    }
}
