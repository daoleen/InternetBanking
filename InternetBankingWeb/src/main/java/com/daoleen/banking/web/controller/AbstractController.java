package com.daoleen.banking.web.controller;

import com.daoleen.banking.web.infrastructure.ViewResult;
import com.daoleen.banking.web.infrastructure.beans.ValidationErrors;
import com.daoleen.banking.web.infrastructure.beans.impl.ValidationErrorsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Created by alex on 1/16/15.
 */
public abstract class AbstractController implements Serializable {
    private final static Logger logger = LoggerFactory.getLogger(AbstractController.class);
    private static final long serialVersionUID = 537290677844726891L;
    protected ValidationErrors validationErrors;
    protected ViewResult viewResult;
    protected HttpServletRequest request;
    protected HttpServletResponse response;

    public AbstractController() {
        this.validationErrors = new ValidationErrorsImpl();
        this.viewResult = new ViewResult("index").add("errors", this.validationErrors);
    }

    public ValidationErrors getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Map<String, Set<ConstraintViolation<Object>>> validationErrors) {
        this.validationErrors.setValidationErrors(validationErrors);
    }

    public ViewResult getViewResult() {
        return viewResult;
    }

    public void setViewResult(ViewResult viewResult) {
        this.viewResult = viewResult;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }
}
