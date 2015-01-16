package com.daoleen.banking.web.infrastructure;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * Created by alex on 1/16/15.
 *
 * General request information:
 * URL: http://localhost:8080/InternetBankingWeb/AbstractServlet/Home/About?xml=false
 * PathInfo: /Home/About
 * ContextPath: /InternetBankingWeb
 * Method: GET
 * RequestURI: /InternetBankingWeb/AbstractServlet/Home/About
 * QueryString: xml=false
 * ServletPath: /AbstractServlet
 */
public class AbstractServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String controllerNamingTemplate = "com.daoleen.banking.web.controller.%sController";
        String[] path = request.getPathInfo().split("/");
        String controller;
        String method;

        if(path == null) {
            controller = "Home";
            method = "index";
        }
        else {
            controller = StringUtils.capitalize(path[0]);
            method = (path.length > 1) ? path[1] : "index";
        }

        try {
            Object controllerInstance = Class.forName(String.format(controllerNamingTemplate, controller)).newInstance();
            Method methodInv = controllerInstance.getClass().getMethod(method);
            Parameter[] methodParameters = methodInv.getParameters();
            int methodParameterCount = methodInv.getParameterCount();

            Map<String, Set<ConstraintViolation<Object>>> validateResult = new HashMap<>(1);
            validateResult.put("validationResult", null /* true|false */);
            List<Object> params = new ArrayList<>();

            if (methodParameterCount > 0) {
                for (Parameter p : methodParameters) {
                    String parameterName = p.getName();
                    Object parameterObject;

                    // TODO: perform binding
                    if (!p.getType().isPrimitive()) {
                        // perform binding
                        parameterObject = p.getType().newInstance();
                        Field[] parameterObjectFields = p.getType().getFields();
                        for (Field f : parameterObjectFields) {
                            String fieldName = f.getName();
                            String fieldValue = request.getParameter(fieldName);

                            if (fieldValue == null) {
                                throw new InitializationControllerException("Can not find parameter with name " + fieldName
                                        + " to instantiating a new object of type " + p.getType().getName());
                            }

                            f.set(parameterObject, fieldValue);
                        }


                        // TODO: perform validation if @Validate present
                        if (p.isAnnotationPresent(Validate.class)) {
                            //
                            //@Inject
                            Validator validator = null;
                            Set<ConstraintViolation<Object>> constraintViolations = validator.validate(parameterObject);
                            if (!constraintViolations.isEmpty()) {
                                // TODO: при рефакторинге заюзать этот ексепшн
                                //throw new ArgumentValidationException(constraintViolations);

                                // TODO: validationResult = false and add constraintViolations
                            }
                        }
                    } else {
                        // TODO: code duplicate
                        parameterObject = request.getParameter(parameterName);
                        if (parameterObject == null) {
                            throw new InitializationControllerException("Can not find parameter with name: " + parameterName);
                        }
                    }


                    // add parameterObject to ordered set
                    params.add(parameterObject);
                }
            }


            // method invocation
            // invokeResult - its View_Name (string) in Spring
            Object invokeResult = methodInv.invoke(controllerInstance, params);

            // TODO: invoke templateResolver here (later in servlet, after refactoring)


            //} catch (ArgumentValidationException e) {
            // logger.info("Invalid form object was received");
            // throw e;
        } catch (InitializationControllerException e) {
            //logger.error('....');
            //throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


//Class.forName("com.daoleen.web.controllers.%sController").newInstance();
//        Object result = Class.forName("   ").getMethod("mymethod").invoke(myClass);