package com.daoleen.banking.web.viewModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by alex on 1/18/15.
 */
public class ClientAddressViewModel {
    @NotNull
    @Size(min = 2, max = 255, message = "Длина поля должна быть между {min} и {max}")
    private String street;

    @Max(value = 1000, message = "Значение не может превышать {value}")
    @Min(value = 1, message = "Значение не может быть меньше {value}")
    private int houseNumber;

    @Max(value = 10, message = "Значение не может превышать {value}")
    private int housingNumber;

    @Max(value = 1000, message = "Значение не может превышать {value}")
    @Min(value = 1, message = "Значение не может быть меньше {value}")
    private int apartmentNumber;


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = Integer.parseInt(houseNumber);
    }

    public int getHousingNumber() {
        return housingNumber;
    }

    public void setHousingNumber(String housingNumber) {
        this.housingNumber = Integer.parseInt(housingNumber);
    }

    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = Integer.parseInt(apartmentNumber);
    }
}
