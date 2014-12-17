package com.daoleen.banking.domain;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
@Entity
@Table(name = "client_address")
@NamedQueries(
        @NamedQuery(name = "findByAddress", query = "select a from ClientAddress a where a.city.id = :cityId and a.street = :street and a.houseNumber = :houseNumber and a.apartmentNumber = :apartmentNumber")
)
public class ClientAddress implements Identifiable<Long>, Serializable {
    private static final long serialVersionUID = 7219687022417992647L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 255, message = "Длина поля должна быть между {min} и {max}")
    @Column(name = "street")
    private String street;

    @Max(value = 1000, message = "Значение не может превышать {value}")
    @Min(value = 1, message = "Значение не может быть меньше {value}")
    @Column(name = "house_number")
    private int houseNumber;

    @Max(value = 10, message = "Значение не может превышать {value}")
    @Column(name = "housing_number")
    private int housingNumber;

    @Max(value = 1000, message = "Значение не может превышать {value}")
    @Min(value = 1, message = "Значение не может быть меньше {value}")
    @Column(name = "apartment_number")
    private int apartmentNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Client> clients;

    public ClientAddress() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientAddress address = (ClientAddress) o;

        if (id != null ? !id.equals(address.id) : address.id != null) return false;

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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public int getHousingNumber() {
        return housingNumber;
    }

    public void setHousingNumber(int housingNumber) {
        this.housingNumber = housingNumber;
    }

    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(int apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
