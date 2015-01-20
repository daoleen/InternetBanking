package com.daoleen.banking.web.controller;

import com.daoleen.banking.domain.Bank;
import com.daoleen.banking.domain.PaymentCard;
import com.daoleen.banking.exception.NoEnoughMoneyException;
import com.daoleen.banking.exception.PaymentTransactionException;
import com.daoleen.banking.repository.remote.*;
import com.daoleen.banking.web.infrastructure.BankUserAuthenticationToken;
import com.daoleen.banking.web.infrastructure.ViewResult;
import com.daoleen.banking.web.infrastructure.annotations.Get;
import com.daoleen.banking.web.infrastructure.annotations.Post;
import com.daoleen.banking.web.infrastructure.annotations.Var;
import com.daoleen.banking.web.infrastructure.domain.BankUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by alex on 1/20/15.
 */
@ApplicationScoped
public class TransferMoneyController extends AbstractController {
    private final static Logger logger = LoggerFactory.getLogger(TransferMoneyController.class);

    @EJB(lookup = "java:global/InternetBankingEJB/PaymentCardBean!com.daoleen.banking.repository.remote.PaymentCardRepositoryRemote")
    private PaymentCardRepositoryRemote paymentCardRepository;

    @EJB(lookup = "java:global/InternetBankingEJB/MoneyReservationBean!com.daoleen.banking.repository.remote.MoneyReservationRepositoryRemote")
    private MoneyReservationRepositoryRemote moneyReservationRepository;

    @EJB(lookup = "java:global/InternetBankingEJB/BankBean!com.daoleen.banking.repository.remote.BankRepositoryRemote")
    private BankRepositoryRemote bankRepository;

    @EJB(lookup = "java:global/InternetBankingEJB/UserBean!com.daoleen.banking.repository.remote.UserRepositoryRemote")
    private UserRepositoryRemote userRepository;

    @EJB(lookup = "java:global/InternetBankingEJB/TransferMoneyBean!com.daoleen.banking.repository.remote.TransferMoneyRepositoryRemote")
    private TransferMoneyRepositoryRemote transferMoneyRepository;


    @Get
    public ViewResult selectCard(@Var("type") String type) {
        BankUser bankUser = getBankUser();
        List<PaymentCard> cards = paymentCardRepository.findByUsername(bankUser.getUsername());
        List<Bank> banks = bankRepository.findAll();
        return viewResult.add("type", type).add("cards", cards).add("banks", banks).setViewName("transfer/selectCard");
    }

    @Post
    public ViewResult selectCard(@Var("cardNumber") String cardNumber, @Var("type") String type, @Var("bic") int bic, @Var("amount") double amount) throws IOException {
        logger.info("cardNumber:{}, bic:{}, amount:{}", cardNumber, bic, amount);
        // TODO: не забыть проверить, что выбранная карта действительно приналделит владельцу текущего акканута
        try {
            transferMoneyRepository.createTransaction(bic, cardNumber, amount);
            String fillFormAddress = String.format("/InternetBankingWeb/app/TransferMoney/%s", type.equals("card") ? "card" : "account");
            response.sendRedirect(fillFormAddress);
            viewResult.setViewName("error");
        } catch (PaymentTransactionException e) {
            viewResult.add("error", "Увы, возникла ошибка. Попробуйте попозже: " + e.getMessage());
        } catch (NoEnoughMoneyException e) {
            viewResult.add("error", "К сожалению, на вашей карте недостаточно средств для совершения перевода");
        }
        return viewResult.setViewName("transfer/selectCard");
    }

    @Get
    public ViewResult card() {
        List<PaymentCard> cards = paymentCardRepository.findAll().stream().filter(c ->
                        c.getBank().getId() == transferMoneyRepository.getPaymentTransaction().getRecepientBankId()
                                && !c.getCardNumber().equals(transferMoneyRepository.getPaymentTransaction().getCard().getCardNumber())
        ).collect(Collectors.toList());
        return viewResult.add("cards", cards).setViewName("transfer/selectRecipientCard");
    }

    @Post
    public ViewResult card(@Var("cardNumber") String cardNumber) throws IOException {
        transferMoneyRepository.fillDataForCardTransfer(cardNumber);
        response.sendRedirect("/InternetBankingWeb/app/TransferMoney/complete");
        return viewResult.setViewName("error");
    }


    @Get
    public ViewResult account() {
        return viewResult.setViewName("transfer/account");
    }


    @Post
    public ViewResult account(@Var("accountNumber") String account, @Var("firstName") String firstName, @Var("lastName") String lastName,
                              @Var("patrName") String patrName) throws IOException {
        transferMoneyRepository.fillDataForMoneyTransfer(account, firstName, lastName, patrName);
        response.sendRedirect("/InternetBankingWeb/app/TransferMoney/complete");
        return viewResult.setViewName("error");
    }

    @Get
    public ViewResult complete() throws IOException {
        // TODO: не забыть сделать подтверждение через смс
        try {
            transferMoneyRepository.completeProcessing();
            response.sendRedirect("/InternetBankingWeb/app/TransferMoney/success");
            return viewResult.setViewName("error");
        } catch (PaymentTransactionException e) {
            viewResult.add("error", "К сожалению возникла ошибка обработки транзакции. Попробуйте попозже. " + e.getMessage());
        }
        return viewResult.setViewName("error");
    }


    public ViewResult success() {
        return viewResult.setViewName("transfer/success");
    }


    private BankUser getBankUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        BankUserAuthenticationToken authentication = (BankUserAuthenticationToken) context.getAuthentication();
        return (BankUser) authentication.getPrincipal();
    }
}
