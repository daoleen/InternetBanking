package com.daoleen.banking.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 12/17/14.
 */
@Entity
@Table(name = "client")
@NamedQueries({
        @NamedQuery(name = Client.FIND_BY_PASSPORT, query = "select c from Client c where c.passportSeries = :series and c.passportNumber = :number"),
        @NamedQuery(name = Client.FIND_BY_MOBILE_NUMBER, query = "select c from Client c where c.mobileNumber = :mobile"),
        @NamedQuery(name = Client.FIND_BY_ADDRESS_ID, query = "select c from Client c where c.address.id = :addressId"),
        @NamedQuery(name = Client.FIND_BY_ADDRESS, query = "select c from Client c where c.address.city.name = :city and c.address.street = :street and c.address.houseNumber = :houseNumber and c.address.apartmentNumber = :apartmentNumber"),
        @NamedQuery(name = Client.FIND_BY_FIO, query = "select c from Client c where c.firstName = :firstName and c.lastName = :lastName and c.patronymicName = :patronymicName")
})
public class Client implements Identifiable<Long>, Serializable {
    public final static String FIND_BY_PASSPORT = "Client.findByPassport";
    public final static String FIND_BY_MOBILE_NUMBER = "Client.findByMobileNumber";
    public final static String FIND_BY_ADDRESS_ID = "Client.findByAddressId";
    public final static String FIND_BY_ADDRESS = "Client.findbyAddress";
    public final static String FIND_BY_FIO = "Client.findByFIO";
    private static final long serialVersionUID = -422999179861103398L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 32, message = "Длина поля должна быть между {min} и {max} символов")
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 32, message = "Длина поля должна быть между {min} и {max} символов")
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Size(min = 2, max = 32, message = "Длина поля должна быть между {min} и {max} символов")
    @Column(name = "patronymic_name")
    private String patronymicName;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    private Date birthDate;

    @NotNull
    @Size(min = 2, max = 2, message = "Длина поля - {max} символов")
    @Column(name = "passport_series")
    private String passportSeries;

    @NotNull
    @Column(name = "passport_number")
    private int passportNumber;

    @Temporal(TemporalType.TIME)
    @Column(name = "registration_date")
    private Date registrationDate;

    @NotNull
    @Size(min = 7, max = 16, message = "Длина поля должна быть между {min} и {max}")
    @Column(name = "mobile_number")
    private String mobileNumber;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private ClientAddress address;

    @OneToOne(mappedBy = "client", fetch = FetchType.LAZY, orphanRemoval = true)
    private User user;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PaymentCard> paymentCards;


    public Client() {
    }

    public Client(String firstName, String lastName, String patronymicName, Date birthDate, String passportSeries, int passportNumber, Date registrationDate, String mobileNumber, ClientAddress address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymicName = patronymicName;
        this.birthDate = birthDate;
        this.passportSeries = passportSeries;
        this.passportNumber = passportNumber;
        this.registrationDate = registrationDate;
        this.mobileNumber = mobileNumber;
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (!id.equals(client.id)) return false;

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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
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

    public void setPassportNumber(int passportNumber) {
        this.passportNumber = passportNumber;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public ClientAddress getAddress() {
        return address;
    }

    public void setAddress(ClientAddress address) {
        this.address = address;
    }
}
