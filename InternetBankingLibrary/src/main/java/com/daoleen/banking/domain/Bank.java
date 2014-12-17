package com.daoleen.banking.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by alex on 12/17/14.
 */
@Entity
@Table(name = "bank")
@NamedQueries({
        @NamedQuery(name = "findByBankAccountNumber", query = "select b from Bank b where b.bankAccountNumber = :bankAccountNumber")
})
public class Bank implements Identifiable<Integer>, Serializable {
    private static final long serialVersionUID = -4659949543452503136L;

    @Id
    @NotNull
    @Column(name = "bic")
    private Integer bic;

    @NotNull
    @Size(min = 2, max = 32, message = "Длина поля должна быть между {min} и {max} символами")
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "bank_account_number")
    private Long bankAccountNumber;

    public Bank() {
    }

    public Bank(Integer bic, String name, Long bankAccountNumber) {
        this.bic = bic;
        this.name = name;
        this.bankAccountNumber = bankAccountNumber;
    }

    @Override
    public Integer getId() {
        return bic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bank bank = (Bank) o;

        if (!bic.equals(bank.bic)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return bic.hashCode();
    }


    public Integer getBic() {
        return bic;
    }

    public void setBic(Integer bic) {
        this.bic = bic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(Long bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }
}
