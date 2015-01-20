package com.daoleen.banking.web.viewModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by alex on 1/19/15.
 */
public class UserViewModel {
    @NotNull
    @Size(min = 4, max = 32, message = "Длина поля должна быть между {min} и {max} символами")
    private String username;

    @NotNull
    private String password;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
