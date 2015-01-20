package com.daoleen.banking.web.viewModel;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * Created by alex on 1/18/15.
 */
public class CityViewModel {
    @NotEmpty
    @Size(min = 3, max = 64, message = "Длина поля должна быть между {min} и {max}")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
