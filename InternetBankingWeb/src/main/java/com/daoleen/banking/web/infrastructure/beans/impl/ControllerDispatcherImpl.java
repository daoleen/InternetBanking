package com.daoleen.banking.web.infrastructure.beans.impl;

import com.daoleen.banking.web.controller.AbstractController;
import com.daoleen.banking.web.infrastructure.ArgumentValidationException;
import com.daoleen.banking.web.infrastructure.InitializationControllerException;
import com.daoleen.banking.web.infrastructure.ViewResult;
import com.daoleen.banking.web.infrastructure.beans.ControllerDispatcher;
import com.daoleen.banking.web.infrastructure.beans.ParametersBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * Created by alex on 1/16/15.
 */
@Singleton
public class ControllerDispatcherImpl implements ControllerDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(ControllerDispatcherImpl.class);
    private static final String controllerNamingTemplate = "com.daoleen.banking.web.controller.%sController";


    @Inject
    private ParametersBinder parametersBinder;

    @Override
    public ViewResult invokeAction(HttpServletRequest request, String controllerClassName, String methodName,
                                   Class<? extends Annotation> annotationRequestType)
            throws InitializationControllerException {
        AbstractController controllerInstance = createControllerInstance(controllerClassName);
        Method methodInv = createActionInstance(controllerInstance, methodName, annotationRequestType);
        Parameter[] methodParameters = methodInv.getParameters();
        int methodParameterCount = methodInv.getParameterCount();
        List<Object> params = null;

        if (methodParameterCount > 0) {
            try {
                params = parametersBinder.bind(methodParameters, request);
            } catch (ArgumentValidationException e) {
                params = e.getParams();
                try {
                    //controllerInstance.getClass().getField("validationErrors").set(controllerInstance, e.getValidationErrors());
                    getMethod(controllerInstance, "setValidationErrors").invoke(controllerInstance, e.getValidationErrors());
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
                    throw new InitializationControllerException(e1.getMessage());
                }
            }
        }

        // method invocation
        System.out.println(params);
        try {
            return (ViewResult) (params != null
                    ? methodInv.invoke(controllerInstance, params.toArray())
                    : methodInv.invoke(controllerInstance)
            );
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("Error while executing method", e);
            throw new InitializationControllerException(e.getMessage());
        }
    }


    private AbstractController createControllerInstance(String controllerClassName) throws InitializationControllerException {
        try {
            return (AbstractController) Class.forName(String.format(controllerNamingTemplate, controllerClassName)).newInstance();
        } catch (Exception e) {
            String message = String.format("An %s was occurred while trying to create a new controller object. Message: %s",
                    e.getClass().getName(), e.getMessage()
            );
            logger.error(message, e);
            throw new InitializationControllerException(message);
        }
    }

    private Method createActionInstance(AbstractController controllerInstance, String methodName,
                                        Class<? extends Annotation> annotationRequestType)
            throws InitializationControllerException {
        Method[] methods = controllerInstance.getClass().getMethods();

        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                if (method.getAnnotation(annotationRequestType) != null) {
                    return method;
                }
            }
        }

        String message = String.format("Could not find method with name %s and request annotation @%s in %s controller",
                methodName, annotationRequestType.getName(), controllerInstance.getClass().getName());
        logger.error(message);
        throw new InitializationControllerException(message);
    }


    private Method getMethod(Object o, String name) throws NoSuchMethodException {
        Method[] methods = o.getClass().getMethods();
        for (Method m : methods) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        throw new NoSuchMethodException("Method " + name + " in " + o.getClass().getName() + " can not be found");
    }
}
