package com.daoleen.banking.web.servlet;

import nz.net.ultraq.thymeleaf.LayoutDialect;
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
        resolver.setPrefix("/views/");
        resolver.setSuffix(".html");
        resolver.setCacheTTLMs(300000L);
        resolver.setCacheable(false);   // todo: no caching at development time!

        TemplateEngine templateEngine = new TemplateEngine();
        SpringSecurityDialect springSecurityDialect = new SpringSecurityDialect();
        LayoutDialect layoutDialect = new LayoutDialect();
        templateEngine.addDialect(springSecurityDialect);
        templateEngine.addDialect(layoutDialect);
        templateEngine.setTemplateResolver(resolver);

        WebContext wc = new WebContext(request, response, getServletContext(), request.getLocale());
        wc.setVariable("name", "Mock");

        templateEngine.process("home", wc, response.getWriter());
    }
}
