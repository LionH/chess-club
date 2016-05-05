package org.chesscorp.club.model.people;

import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Objects;

/**
 * Player data model. Can be used for actual players as well as robot players.
 *
 * @author Yannick Kirschhoffer <alcibiade@alcibiade.org>
 */
@Entity
@Proxy(lazy = false)
@Table(
        indexes = {
                @Index(columnList = "displayName", unique = false),
                @Index(columnList = "normalizedName", unique = false)
        }
)
@SequenceGenerator(name = "player_seq", initialValue = 1, allocationSize = 1, sequenceName = "player_seq")
public abstract class Player implements Comparable<Player> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "player_seq")
    private Long id;
    @Column(nullable = false, length = 32)
    private String displayName;

    public Player() {
    }

    public Player(String displayName) {
        this.displayName = displayName;
    }

    public Long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
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
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int compareTo(Player o) {
        return getDisplayName().compareTo(o.getDisplayName());
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
