package com.daoleen.banking.web.infrastructure.beans.impl;

import com.daoleen.banking.web.controller.AbstractController;
import com.daoleen.banking.web.infrastructure.ArgumentValidationException;
import com.daoleen.banking.web.infrastructure.InitializationControllerException;
import com.daoleen.banking.web.infrastructure.ViewResult;
import com.daoleen.banking.web.infrastructure.beans.ControllerDispatcher;
import com.daoleen.banking.web.infrastructure.beans.ParametersBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Set;

/**
 * Created by alex on 1/16/15.
 */
@Singleton
public class ControllerDispatcherImpl implements ControllerDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(ControllerDispatcherImpl.class);
    private static final String controllerNamingTemplate = "com.daoleen.banking.web.controller.%sController";


    @Inject
    private ParametersBinder parametersBinder;


    /**
     * Creates a new managed controller instance by specified controller name
     * and invokes a specified action (which is method in controller) with specified request annotation type
     *
     * @param request               the HttpServletRequest
     * @param controllerClassName   the full name of controller class
     * @param methodName            the method name
     * @param annotationRequestType one of annotation type either @Get or @Post
     * @return the ViewResult object describes the template view name and template variables
     * @throws InitializationControllerException
     */
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
                    getMethod(controllerInstance, "setValidationErrors").invoke(controllerInstance, e.getValidationErrors());
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
                    throw new InitializationControllerException(e1.getMessage());
                }
            }
        }

        // method invocation
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


    /**
     * Creates a Managed controller instance.
     * Therefore it can be possible to achieve the DI capabilities in that Controller,
     * for example using @EJB annotation
     *
     * @param controllerClassName the full name of controller class
     * @return the managed instance of controller
     * @throws InitializationControllerException uf such controller class could not be found
     */
    @Override
    public AbstractController createControllerInstance(String controllerClassName) throws InitializationControllerException {
        String controllerName = String.format(controllerNamingTemplate, controllerClassName);

        try {
            BeanManager beanManager = (BeanManager) new InitialContext().lookup("java:comp/BeanManager");
            Set<Bean<?>> beans = beanManager.getBeans(Class.forName(controllerName));

            if (beans == null || beans.isEmpty()) {

            }

            Bean<?> controller = beans.iterator().next();
            CreationalContext<?> creationalContext = beanManager.createCreationalContext(controller);
            return (AbstractController) beanManager.getReference(controller, controller.getBeanClass(), creationalContext);
        } catch (ClassNotFoundException | NamingException e) {
            String msg = String.format("Could not find a controller with name '%s'", controllerName);
            logger.error(msg);
            throw new InitializationControllerException(msg);
        }
    }


    /**
     * Creates a method instance for specified controller instance by specified method name and request annotation
     *
     * @param controllerInstance    the controller object
     * @param methodName            the method name
     * @param annotationRequestType one of annotation type either @Get or @Post
     * @return the method instance
     * @throws InitializationControllerException if such method could not be found
     */
    @Override
    public Method createActionInstance(AbstractController controllerInstance, String methodName,
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
