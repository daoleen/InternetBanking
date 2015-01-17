package com.daoleen.banking.web.infrastructure.beans;

import com.daoleen.banking.web.infrastructure.InitializationControllerException;
import com.daoleen.banking.web.infrastructure.ViewResult;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

/**
 * Created by alex on 1/16/15.
 */
public interface ControllerDispatcher {
    public ViewResult invokeAction(HttpServletRequest request, String controllerClassName, String methodName, Class<? extends Annotation> annotationRequestType) throws InitializationControllerException;
}
