package com.daoleen.banking.web.servlet;

import com.daoleen.banking.web.infrastructure.InitializationControllerException;
import com.daoleen.banking.web.infrastructure.ViewResult;
import com.daoleen.banking.web.infrastructure.beans.ControllerDispatcher;
import com.daoleen.banking.web.infrastructure.beans.TemplateProcessor;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by alex on 1/16/15.
 * <p/>
 * General request information:
 * URL: http://localhost:8080/InternetBankingWeb/AbstractServlet/Home/About?xml=false
 * PathInfo: /Home/About
 * ContextPath: /InternetBankingWeb
 * Method: GET
 * RequestURI: /InternetBankingWeb/AbstractServlet/Home/About
 * QueryString: xml=false
 * ServletPath: /AbstractServlet
 */
public class DispatcherServlet extends HttpServlet {

    @Inject
    private ControllerDispatcher controllerDispatcher;

    @Inject
    private TemplateProcessor templateProcessor;


    // TODO: add me to web.xml

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] controllerMethod = getControllerMethod(request);
        ViewResult viewResult = null;

        try {
            System.out.println("controllerMethod[0] is: " + controllerMethod[0]);
            System.out.println("controllerMethod[1] is: " + controllerMethod[1]);
            viewResult = controllerDispatcher.invokeAction(request, controllerMethod[0], controllerMethod[1]);
        } catch (InitializationControllerException e) {
            e.printStackTrace();
        }

        templateProcessor.process(request, response, getServletContext(), viewResult);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] controllerMethod = getControllerMethod(request);
        ViewResult viewResult = null;

        try {
            viewResult = controllerDispatcher.invokeAction(request, controllerMethod[0], controllerMethod[1]);
        } catch (InitializationControllerException e) {
            e.printStackTrace();
        }

        templateProcessor.process(request, response, getServletContext(), viewResult);
    }

    private String[] getControllerMethod(HttpServletRequest request) {
        System.out.println("pathInfo is: " + request.getPathInfo());
        String[] controllerMethod = new String[2];
        String[] path = request.getPathInfo().split("/");

        if (path == null || path.length < 2) {
            controllerMethod[0] = "Home";
            controllerMethod[1] = "index";
        } else {
            controllerMethod[0] = StringUtils.capitalize(path[1]);
            controllerMethod[1] = (path.length > 1) ? path[2] : "index";
        }
        return controllerMethod;
    }
}