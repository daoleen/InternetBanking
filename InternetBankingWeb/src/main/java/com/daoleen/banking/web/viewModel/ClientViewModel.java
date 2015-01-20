package com.daoleen.banking.web.viewModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by alex on 1/19/15.
 */
public class ClientViewModel {
    @NotNull
    @Size(min = 2, max = 32, message = "Длина поля должна быть между {min} и {max} символов")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 32, message = "Длина поля должна быть между {min} и {max} символов")
    private String lastName;

    @NotNull
    @Size(min = 2, max = 32, message = "Длина поля должна быть между {min} и {max} символов")
    private String patronymicName;

    @NotNull
    @Size(min = 2, max = 2, message = "Длина поля - {max} символов")
    private String passportSeries;

    @NotNull
    private int passportNumber;

    @NotNull
    @Size(min = 7, max = 16, message = "Длина поля должна быть между {min} и {max}")
    private String mobileNumber;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymicName() {
        return patronymicName;
    }

    public void setPatronymicName(String patronymicName) {
        this.patronymicName = patronymicName;
    }

    public String getPassportSeries() {
        return passportSeries;
    }

    public void setPassportSeries(String passportSeries) {
        this.passportSeries = passportSeries;
    }

    public int getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = Integer.parseInt(passportNumber);
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
