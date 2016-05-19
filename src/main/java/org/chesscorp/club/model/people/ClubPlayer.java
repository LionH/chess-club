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
    @Column(length = 64)
    private String avatarHash;

    public ClubPlayer() {
    }

    public ClubPlayer(String displayName) {
        this(displayName, null);
    }

    public ClubPlayer(String displayName, String avatarHash) {
        this(displayName, avatarHash, true);
    }

    public ClubPlayer(String displayName, String avatarHash, boolean active) {
        super(displayName, OffsetDateTime.now());
        this.active = active;
        this.avatarHash = avatarHash;
    }

    public boolean isActive() {
        return active;
    }

    public String getAvatarHash() {
        return avatarHash;
    }

    @Override
    public String toString() {
        return "ClubPlayer{" +
                "displayName=" + getDisplayName() +
                ", active=" + active +
                '}';
    }
}
