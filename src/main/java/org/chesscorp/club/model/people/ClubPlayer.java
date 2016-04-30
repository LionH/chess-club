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
public class ClubPlayer extends RegisteredPlayer {
    @Column
    private boolean active;

    public ClubPlayer() {
    }

    public ClubPlayer(String displayName) {
        this(displayName, true);
    }

    public ClubPlayer(String displayName, boolean active) {
        super(displayName, OffsetDateTime.now());
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "ClubPlayer{" +
                "displayName=" + getDisplayName() +
                ", active=" + active +
                '}';
    }
}
