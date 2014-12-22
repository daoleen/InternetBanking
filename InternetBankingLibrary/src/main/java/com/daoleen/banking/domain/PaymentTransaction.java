package com.daoleen.banking.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by alex on 12/17/14.
 */
@Entity
@Table(name = "payment_transaction")
@NamedQueries({
        @NamedQuery(name = "findByCardNumber", query = "select t from PaymentTransaction t where t.card.cardNumber = :cardNumber")
})
public class PaymentTransaction implements Identifiable<UUID>, Serializable {
    private static final long serialVersionUID = 3742187395769719621L;

    @Id
    @NotNull
    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "recepient_bank_id")
    private Integer recepientBankId;

    @Size(max = 255, message = "Максимальная длина поля - {max} символов")
    @Column(name = "recepient_account_number")
    private String recepientAccountNumber;

    @Size(max = 32, message = "Максимальная длина поля - {max} символов")
    @Column(name = "recepient_first_name")
    private String recepientFirstName;

    @Size(max = 32, message = "Максимальная длина поля - {max} символов")
    @Column(name = "recepient_last_name")
    private String recepientLastName;

    @Size(max = 32, message = "Максимальная длина поля - {max} символов")
    @Column(name = "recepient_patronymic_name")
    private String recepientPatronymicName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Size(max = 16, message = "Максимальная длина поля - {max} символов")
    @Column(name = "transaction_status")
    private String transactionStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "completed_at")
    private Date completedAt;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recepient_card_number")
    private PaymentCard recepientCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_number")
    private PaymentCard card;

    @OneToOne(mappedBy = "paymentTransaction", orphanRemoval = true)
    private MoneyReservation moneyReservation;


    public PaymentTransaction() {
        this.uuid = UUID.randomUUID();
        this.transactionStatus = "created";
        this.createdAt = new Date();
    }

    public PaymentTransaction(PaymentCard card) {
        this();
        this.card = card;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentTransaction that = (PaymentTransaction) o;

        if (!uuid.equals(that.uuid)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public UUID getId() {
        return uuid;
    }


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Integer getRecepientBankId() {
        return recepientBankId;
    }

    public void setRecepientBankId(Integer recepientBankId) {
        this.recepientBankId = recepientBankId;
    }

    public String getRecepientAccountNumber() {
        return recepientAccountNumber;
    }

    public void setRecepientAccountNumber(String recepientAccountNumber) {
        this.recepientAccountNumber = recepientAccountNumber;
    }

    public String getRecepientFirstName() {
        return recepientFirstName;
    }

    public void setRecepientFirstName(String recepientFirstName) {
        this.recepientFirstName = recepientFirstName;
    }

    public String getRecepientLastName() {
        return recepientLastName;
    }

    public void setRecepientLastName(String recepientLastName) {
        this.recepientLastName = recepientLastName;
    }

    public String getRecepientPatronymicName() {
        return recepientPatronymicName;
    }

    public void setRecepientPatronymicName(String recepientPatronymicName) {
        this.recepientPatronymicName = recepientPatronymicName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public PaymentCard getRecepientCard() {
        return recepientCard;
    }

    public void setRecepientCard(PaymentCard recepientCard) {
        this.recepientCard = recepientCard;
    }

    public PaymentCard getCard() {
        return card;
    }

    public void setCard(PaymentCard card) {
        this.card = card;
    }

    public MoneyReservation getMoneyReservation() {
        return moneyReservation;
    }

    public void setMoneyReservation(MoneyReservation moneyReservation) {
        this.moneyReservation = moneyReservation;
    }
}
