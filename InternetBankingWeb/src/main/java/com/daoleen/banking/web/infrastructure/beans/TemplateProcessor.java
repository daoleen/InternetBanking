package com.daoleen.banking.web.infrastructure.beans;

import com.daoleen.banking.web.infrastructure.ViewResult;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by alex on 1/16/15.
 */
public interface TemplateProcessor {
    public void process(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext, ViewResult viewResult) throws IOException;
}
