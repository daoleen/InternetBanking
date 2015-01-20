package com.daoleen.banking.web.viewModel;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by alex on 1/20/15.
 */
public class PaymentCardViewModel {
    @NotNull
    private String cardNumber;

    @NotNull
    @Min(value = 0, message = "Минимальное значение - 0")
    private double amount;

    private double reservedAmount;

    @NotNull
    private String pinCode;

    private boolean active;

    private String bankName;


    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getReservedAmount() {
        return reservedAmount;
    }

    public void setReservedAmount(double reservedAmount) {
        this.reservedAmount = reservedAmount;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
