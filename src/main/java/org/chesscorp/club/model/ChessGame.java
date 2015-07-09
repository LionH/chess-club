package org.chesscorp.club.model;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Chess game data model.
 *
 * @author Yannick Kirschhoffer <alcibiade@alcibiade.org>
 */
@Entity
public class ChessGame {

    /**
     * Possible game statuses.
     */
    public enum Status {

        OPEN, PAT, WHITEWON, BLACKWON
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @ManyToOne
    private Player whitePlayer;
    @ManyToOne
    private Player blackPlayer;
    @ElementCollection(targetClass = String.class)
    private List<String> moves;
    private Status status;

    public ChessGame() {
    }

    public ChessGame(String id, Player whitePlayer, Player blackPlayer, List<String> moves, Status status) {
        this.id = id;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.moves = moves;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public List<String> getMoves() {
        return Collections.unmodifiableList(moves);
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.id);
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
        final ChessGame other = (ChessGame) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
