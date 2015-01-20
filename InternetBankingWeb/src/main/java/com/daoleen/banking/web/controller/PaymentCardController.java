package com.daoleen.banking.web.controller;

import com.daoleen.banking.domain.Bank;
import com.daoleen.banking.domain.PaymentCard;
import com.daoleen.banking.domain.User;
import com.daoleen.banking.repository.remote.BankRepositoryRemote;
import com.daoleen.banking.repository.remote.MoneyReservationRepositoryRemote;
import com.daoleen.banking.repository.remote.PaymentCardRepositoryRemote;
import com.daoleen.banking.repository.remote.UserRepositoryRemote;
import com.daoleen.banking.web.infrastructure.BankUserAuthenticationToken;
import com.daoleen.banking.web.infrastructure.ViewResult;
import com.daoleen.banking.web.infrastructure.annotations.Get;
import com.daoleen.banking.web.infrastructure.annotations.Post;
import com.daoleen.banking.web.infrastructure.annotations.Var;
import com.daoleen.banking.web.infrastructure.domain.BankUser;
import com.daoleen.banking.web.viewModel.PaymentCardViewModel;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 1/20/15.
 */
@RequestScoped
public class PaymentCardController extends AbstractController {

    @EJB(lookup = "java:global/InternetBankingEJB/PaymentCardBean!com.daoleen.banking.repository.remote.PaymentCardRepositoryRemote")
    private PaymentCardRepositoryRemote paymentCardRepository;

    @EJB(lookup = "java:global/InternetBankingEJB/MoneyReservationBean!com.daoleen.banking.repository.remote.MoneyReservationRepositoryRemote")
    private MoneyReservationRepositoryRemote moneyReservationRepository;

    @EJB(lookup = "java:global/InternetBankingEJB/BankBean!com.daoleen.banking.repository.remote.BankRepositoryRemote")
    private BankRepositoryRemote bankRepository;

    @EJB(lookup = "java:global/InternetBankingEJB/UserBean!com.daoleen.banking.repository.remote.UserRepositoryRemote")
    private UserRepositoryRemote userRepository;

    @Get
    public ViewResult list() {
        BankUser user = getBankUser();
        List<PaymentCard> cards = paymentCardRepository.findByUsername(user.getUsername());
        List<PaymentCardViewModel> cardViewModels = new ArrayList<>(cards.size());

        cards.parallelStream().forEach(c -> {
            PaymentCardViewModel pcvm = new PaymentCardViewModel();
            pcvm.setActive(c.isActive());
            pcvm.setAmount(c.getAmount());
            pcvm.setBankName(c.getBank().getName());
            pcvm.setCardNumber(c.getCardNumber());
            pcvm.setReservedAmount(moneyReservationRepository.getActiveReservationSum(c));
            cardViewModels.add(pcvm);
        });

        return viewResult.add("cards", cardViewModels).setViewName("card/list");
    }


    @Get
    public ViewResult create() {
        List<Bank> banks = bankRepository.findAll();
        return viewResult.add("banks", banks).setViewName("card/create");
    }

    @Post
    public ViewResult create(@Var("bic") int bic, @Var("pinCode") String pinCode) throws IOException {

        // Attention: THREAD UNSAFE!
        int lastCardNumberDigits;
        try {
            List<PaymentCard> cards = paymentCardRepository.findAll();
            PaymentCard lastCard = cards.get(cards.size() - 1);
            String[] cardNumberDigits = lastCard.getCardNumber().split("-");
            lastCardNumberDigits = Integer.parseInt(cardNumberDigits[cardNumberDigits.length - 1]);
        } catch (Exception e) { // indexofbound :))
            lastCardNumberDigits = 1;
        }

        BankUser bankUser = getBankUser();
        User user = userRepository.findByUsername(bankUser.getUsername());
        Bank bank = bankRepository.findById(bic); //Integer.parseInt(bic));

        PaymentCard card = new PaymentCard(String.format("0000-0000-0000-100%d", ++lastCardNumberDigits), 300.00,
                pinCode, new Date(), bank, user.getClient(), true);

        card = paymentCardRepository.save(card);
        response.sendRedirect("/InternetBankingWeb/app/PaymentCard/list");
        return viewResult.setViewName("error");
    }


    private BankUser getBankUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        BankUserAuthenticationToken authentication = (BankUserAuthenticationToken) context.getAuthentication();
        return (BankUser) authentication.getPrincipal();
    }
}
