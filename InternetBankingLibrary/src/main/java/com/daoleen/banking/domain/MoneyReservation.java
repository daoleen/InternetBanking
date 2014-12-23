package com.daoleen.banking.domain;

import com.daoleen.banking.converter.MoneyReservationStatusConverter;
import com.daoleen.banking.enums.MoneyReservationStatus;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by alex on 12/21/14.
 */
@Entity
@Table(name = "money_reservation")
@NamedQueries({
        @NamedQuery(name = "getActiveReservations", query = "select r from MoneyReservation r where r.paymentCard.cardNumber = :cardNumber and r.status = :openedStatus"),
        @NamedQuery(name = "getActiveReservationSum", query = "select sum(r.amount) from MoneyReservation r where r.paymentCard = :card and r.status = :openedStatus"),
        @NamedQuery(name = "closeReservation", query = "update MoneyReservation r set r.status = :closedStatus where r.id = :id")
})
public class MoneyReservation implements Identifiable<Long>, Serializable {
    private static final long serialVersionUID = -6174020065678295704L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(0)
    @Column(name = "amount")
    private Double amount;

    @NotNull
//    @Enumerated(EnumType.STRING)
    @Convert(converter = MoneyReservationStatusConverter.class)
    @Column(name = "status")
    private MoneyReservationStatus status;

    @Temporal(TemporalType.TIME)
    @Column(name = "created_at")
    private Date createdAt;

    @Version
    @Column(name = "version")
    private Long version;


    @ManyToOne
    @JoinColumn(name = "payment_card")
    private PaymentCard paymentCard;

    @OneToOne(mappedBy = "moneyReservation", cascade = CascadeType.ALL)
    private PaymentTransaction paymentTransaction;


    public MoneyReservation() {
        this.status = MoneyReservationStatus.OPENED;
    }

    public MoneyReservation(PaymentCard paymentCard, Double amount) {
        this();
        this.paymentCard = paymentCard;
        this.amount = amount;
    }

    public MoneyReservation(PaymentCard paymentCard, MoneyReservationStatus status, Double amount) {
        this(paymentCard, amount);
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoneyReservation that = (MoneyReservation) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public MoneyReservationStatus getStatus() {
        return status;
    }

    public void setStatus(MoneyReservationStatus status) {
        this.status = status;
    }

    public PaymentCard getPaymentCard() {
        return paymentCard;
    }

    public void setPaymentCard(PaymentCard paymentCard) {
        this.paymentCard = paymentCard;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public PaymentTransaction getPaymentTransaction() {
        return paymentTransaction;
    }

    public void setPaymentTransaction(PaymentTransaction paymentTransaction) {
        this.paymentTransaction = paymentTransaction;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
