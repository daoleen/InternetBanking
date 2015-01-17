package com.daoleen.banking.web.infrastructure.beans.impl;

import com.daoleen.banking.web.infrastructure.ViewResult;
import com.daoleen.banking.web.infrastructure.beans.TemplateProcessor;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.extras.springsecurity3.dialect.SpringSecurityDialect;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by alex on 1/16/15.
 */
@Singleton
public class TemplateProcessorImpl implements TemplateProcessor {
    private final TemplateEngine templateEngine = new TemplateEngine();

    public TemplateProcessorImpl() {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
        resolver.setTemplateMode("HTML5");
        resolver.setPrefix("/views/");
        resolver.setSuffix(".html");
        resolver.setCacheTTLMs(300000L);
        resolver.setCacheable(false);   // todo: no caching at development time!

        SpringSecurityDialect springSecurityDialect = new SpringSecurityDialect();
        LayoutDialect layoutDialect = new LayoutDialect();
        templateEngine.addDialect(springSecurityDialect);
        templateEngine.addDialect(layoutDialect);
        templateEngine.setTemplateResolver(resolver);
    }

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext, ViewResult viewResult)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        System.out.println("viewResult is: " + viewResult);
        WebContext webContext = new WebContext(request, response, servletContext, request.getLocale(), viewResult.getVariables());
        templateEngine.process(viewResult.getViewName(), webContext, response.getWriter());
    }
}
