package com.daoleen.banking.domain;

import com.daoleen.banking.converter.StringToByteArrayConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by alex on 12/17/14.
 */
@Entity
@Table(name = "user")
@NamedQueries({
        @NamedQuery(name = User.FIND_ALL, query = "select u from User u where u.enabled = true and u.blocked = false"),
        @NamedQuery(name = User.FIND_BY_USERNAME, query = "select u from User u where u.username = :username"),
        @NamedQuery(name = User.FIND_BLOCKED, query = "select u from User u where u.blocked = true"),
        @NamedQuery(name = User.FIND_DISABLED, query = "select u from User u where u.enabled = false"),
        @NamedQuery(name = User.DISABLE_USER, query = "update User u set u.enabled = false where u.id = :id"),
        @NamedQuery(name = User.BLOCK_USER, query = "update User u set u.blocked = true, u.blockedReason = :reason where u.id = :id")
})
public class User implements Identifiable<Long>, Serializable {
    public static final String FIND_ALL = "User.findAll";
    public static final String FIND_BY_USERNAME = "User.findByUsername";
    public static final String FIND_BLOCKED = "User.findBlocked";
    public static final String FIND_DISABLED = "User.findDisabled";
    public static final String DISABLE_USER = "User.disableUser";
    public static final String BLOCK_USER = "User.blockUser";
    private static final long serialVersionUID = 1479872066542136816L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 4, max = 32, message = "Длина поля должна быть между {min} и {max} символами")
    @Column(name = "username")
    private String username;

    @NotNull
    @Column(name = "password")
    @Convert(converter = StringToByteArrayConverter.class)
    private String password;

    @Column(name = "is_enabled")
    private boolean enabled;

    @Column(name = "is_blocked")
    private boolean blocked;

    @Size(max = 255, message = "Максимальная длина поля {max} символов")
    @Column(name = "blocked_reason")
    private String blockedReason;



    @NotNull
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "client_id")
    private Client client;


    public User() {
        enabled = true;
        blocked = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getBlockedReason() {
        return blockedReason;
    }

    public void setBlockedReason(String blockedReason) {
        this.blockedReason = blockedReason;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
