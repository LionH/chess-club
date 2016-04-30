package org.chesscorp.club.model.people;

import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.OffsetDateTime;

/**
 * A local club human player.
 *
 * @author Yannick Kirschhoffer <alcibiade@alcibiade.org>
 */
@Entity
@Proxy(lazy = false)
public abstract class RegisteredPlayer extends Player {

    @Column
    private OffsetDateTime registrationDate;

    public RegisteredPlayer() {
    }

    public RegisteredPlayer(OffsetDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public RegisteredPlayer(String displayName, OffsetDateTime registrationDate) {
        super(displayName);
        this.registrationDate = registrationDate;
    }

    public OffsetDateTime getRegistrationDate() {
        return registrationDate;
    }
}
