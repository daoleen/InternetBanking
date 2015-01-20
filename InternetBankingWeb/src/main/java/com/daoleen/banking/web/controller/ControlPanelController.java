package com.daoleen.banking.web.controller;

import com.daoleen.banking.web.infrastructure.ViewResult;
import com.daoleen.banking.web.infrastructure.annotations.Get;

import javax.enterprise.context.RequestScoped;

/**
 * Created by alex on 1/20/15.
 */
@RequestScoped
public class ControlPanelController extends AbstractController {

    @Get
    public ViewResult home() {
        return viewResult.setViewName("control/home");
    }

}
