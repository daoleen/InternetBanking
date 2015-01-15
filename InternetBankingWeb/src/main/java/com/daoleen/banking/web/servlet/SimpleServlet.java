package com.daoleen.banking.web.servlet;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.extras.springsecurity3.dialect.SpringSecurityDialect;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.io.IOException;

/**
 * Created by alex on 1/15/15.
 */
@javax.servlet.annotation.WebServlet(name = "SimpleServlet")
public class SimpleServlet extends javax.servlet.http.HttpServlet {
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
        resolver.setTemplateMode("HTML5");
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".html");
        resolver.setCacheTTLMs(0L);

        TemplateEngine templateEngine = new TemplateEngine();
        SpringSecurityDialect springSecurityDialect = new SpringSecurityDialect();
        templateEngine.addDialect(springSecurityDialect);
        templateEngine.setTemplateResolver(resolver);

        WebContext wc = new WebContext(request, response, getServletContext(), request.getLocale());
        wc.setVariable("name", "Mock");

        templateEngine.process("home", wc, response.getWriter());
    }
}
