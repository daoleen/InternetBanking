package com.daoleen.banking.domain;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
@Entity
@Table(name = "payment_card")
@NamedQueries({
        @NamedQuery(name = "findByBankAndClientPassport", query = "select c from PaymentCard c where c.bank.name = :bankName and c.client.passportSeries = :passportSeries and c.client.passportNumber = :passportNumber")
})
public class PaymentCard implements Identifiable<String>, Serializable {
    private static final long serialVersionUID = 6121569744751124720L;

    @Id
    @NotNull
    @Column(name = "card_number")
    private String cardNumber;

    @NotNull
    @Min(value = 0, message = "Минимальное значение - 0")
    @Column(name = "amount")
    private Double amount;

    @NotNull
    @Column(name = "pin_code")
    private String pinCode;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_date")
    private Date createdDate;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PaymentTransaction> paymentTransactions;


    @Override
    public String getId() {
        return cardNumber;
    }


    public PaymentCard() {
    }

    public PaymentCard(String cardNumber, Double amount, String pinCode, Date createdDate, Bank bank, Client client) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.pinCode = pinCode;
        this.createdDate = createdDate;
        this.bank = bank;
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentCard that = (PaymentCard) o;

        if (!cardNumber.equals(that.cardNumber)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return cardNumber.hashCode();
    }


    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
