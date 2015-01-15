package com.daoleen.banking.web.infrastructure;

import org.thymeleaf.TemplateEngine;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created by alex on 1/15/15.
 */
@WebFilter(filterName = "BankingWebFilter")
public class BankingWebFilter implements Filter {

    private TemplateEngine templateEngine;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        chain.doFilter(req, resp);


        //
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
