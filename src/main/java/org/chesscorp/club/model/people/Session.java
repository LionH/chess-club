package org.chesscorp.club.model.people;

import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.OffsetDateTime;
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
    @Column(length = 64)
    private String authenticationToken;
    @ManyToOne(optional = false)
    private Account account;
    @Column(nullable = false)
    private OffsetDateTime creationTime;


    public Session() {
    }

    public Session(String authenticationToken, Account account) {
        this.authenticationToken = authenticationToken;
        this.account = account;
        this.creationTime = OffsetDateTime.now();
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public Account getAccount() {
        return account;
    }

    public OffsetDateTime getCreationTime() {
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
