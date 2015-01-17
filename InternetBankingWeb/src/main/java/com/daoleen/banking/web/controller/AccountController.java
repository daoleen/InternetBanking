package com.daoleen.banking.web.controller;

import com.daoleen.banking.web.infrastructure.ViewResult;
import com.daoleen.banking.web.infrastructure.annotations.Get;

/**
 * Created by alex on 1/17/15.
 */
public class AccountController extends AbstractController {

    @Get
    public ViewResult signin() {
        return viewResult.setViewName("account/signin");
    }

}
