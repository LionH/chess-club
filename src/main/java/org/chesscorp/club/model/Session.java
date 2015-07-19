package org.chesscorp.club.model;

import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Player data model. Can be used for actual players as well as robot players.
 *
 * @author Yannick Kirschhoffer <alcibiade@alcibiade.org>
 */
@Entity
@Proxy(lazy = false)
public class Session {

    @Id
    private String authenticationToken;
    @ManyToOne
    private Account account;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;


    public Session() {
    }

    public Session(String authenticationToken, Account account) {
        this.authenticationToken = authenticationToken;
        this.account = account;
        this.creationTime = new Date();
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public Account getAccount() {
        return account;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(authenticationToken, session.authenticationToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authenticationToken);
    }

    @Override
    public String toString() {
        return "Session{" +
                "authenticationToken='" + authenticationToken + '\'' +
                ", account=" + account +
                ", creationTime=" + creationTime +
                '}';
    }
}
