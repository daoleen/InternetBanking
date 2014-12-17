package com.daoleen.banking.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Created by alex on 12/6/14.
 */
@Entity
@Table(name = "city")
@NamedQueries({
        @NamedQuery(name = "findByName", query = "select c from City c where c.name = :name")
})
public class City implements Identifiable<Integer>, Serializable {
    private static final long serialVersionUID = 3460842343712412283L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotEmpty
    @Size(min = 3, max = 64, message = "Длина поля должна быть между {min} и {max}")
    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ClientAddress> clientAddresses;

    public City() {}

    public City(String name) {
        this.name = name;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ClientAddress> getClientAddresses() {
        return clientAddresses;
    }

    public void setClientAddresses(List<ClientAddress> clientAddresses) {
        this.clientAddresses = clientAddresses;
    }

    @Override
    public String toString() {
        return String.format("City{ id = %d, name = %s }", id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        if (id != null ? !id.equals(city.id) : city.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
