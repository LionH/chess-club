package org.chesscorp.club.model.people;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * A player that is not registered on the club but has games registered in the system.
 *
 * @author Yannick Kirschhoffer <alcibiade@alcibiade.org>
 */
@Entity
public class ExternalPlayer extends Player {
    @Column(length = 32)
    private String normalizedName;

    public ExternalPlayer() {
    }

    public ExternalPlayer(String displayName, String normalizedName) {
        super(displayName);
        this.normalizedName = normalizedName;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    @Override
    public String toString() {
        return "ExternalPlayer{" +
                "displayName=" + getDisplayName() +
                "normalizedName=" + normalizedName +
                '}';
    }
}
