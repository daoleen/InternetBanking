package com.daoleen.banking.web.controller;

import com.daoleen.banking.repository.remote.PaymentCardRepositoryRemote;
import com.daoleen.banking.web.infrastructure.ViewResult;
import com.daoleen.banking.web.infrastructure.annotations.Get;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 * Created by alex on 1/20/15.
 */
@RequestScoped
public class PaymentCardController extends AbstractController {

    @EJB
    private PaymentCardRepositoryRemote paymentCardRepository;

    @Get
    public ViewResult list() {
        //paymentCardRepository.f
        return null;
    }

}
