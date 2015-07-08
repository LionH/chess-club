package org.chesscorp.club.model;

import java.util.Objects;

/**
 * Player data model. Can be used for actual players as well as robot players.
 *
 * @author Yannick Kirschhoffer <alcibiade@alcibiade.org>
 */
public class Player {

    private String id;
    private String fullName;

    public Player(String id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Player other = (Player) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
