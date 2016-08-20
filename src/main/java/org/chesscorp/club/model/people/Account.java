package org.chesscorp.club.model.people;

import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

/**
 * Player data model. Can be used for actual players as well as robot players.
 *
 * @author Yannick Kirschhoffer <alcibiade@alcibiade.org>
 */
@Entity
@Proxy(lazy = false)
public class Account {

    @Id
    @Column(length = 128)
    private String identifier;
    @Column(nullable = false, length = 32)
    private String salt;
    @Column(nullable = false, length = 32)
    private String password;
    @ManyToOne(optional = false)
    private Player player;
    @Column(nullable = false)
    private Boolean validated;

    public Account() {
    }

    public Account(String identifier, String salt, String password, Player player) {
        this.identifier = identifier;
        this.salt = salt;
        this.password = password;
        this.player = player;
        this.validated = false;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(identifier, account.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    @Override
    public String toString() {
        return "Account{" +
                "identifier='" + identifier + '\'' +
                ", password='" + password + '\'' +
                ", player=" + player +
                '}';
    }
}
