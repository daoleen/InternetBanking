package com.daoleen.banking.web.controller;

import com.daoleen.banking.web.infrastructure.ViewResult;

import javax.validation.ConstraintViolation;
import java.util.Map;
import java.util.Set;

/**
 * Created by alex on 1/16/15.
 */
public abstract class AbstractController {
    protected Map<String, Set<ConstraintViolation<Object>>> validationErrors;
    protected ViewResult viewResult = new ViewResult("index");

    public Map<String, Set<ConstraintViolation<Object>>> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Map<String, Set<ConstraintViolation<Object>>> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public ViewResult getViewResult() {
        return viewResult;
    }

    public void setViewResult(ViewResult viewResult) {
        this.viewResult = viewResult;
    }
}
