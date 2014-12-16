package com.daoleen.banking.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by alex on 12/6/14.
 */
@Entity
@Table(name = "city")
@NamedQueries({
        @NamedQuery(name = "findByName", query = "select c from City c where c.name = :name")
})
public class City extends BaseEntity<Integer> implements Serializable {
    private static final long serialVersionUID = 3460842343712412283L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty
    @Size(min = 3, max = 64, message = "Длина поля должна быть между {min} и {max}")
    @Column(name = "name", unique = true)
    private String name;


    @Override
    public Integer getIdentifier() {
        if(id == 0) return null;
        return id;
    }

    public City() {}

    public City(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("City{ id = %d, name = %s }", id, name);
    }
}
