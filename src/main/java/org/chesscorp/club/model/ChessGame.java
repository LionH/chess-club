package org.chesscorp.club.model;

import javax.persistence.*;
import java.util.*;

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
    @ElementCollection(targetClass = ChessMove.class)
    private List<ChessMove> moves;
    private Date startDate;
    private Status status;

    public ChessGame() {
    }

    public ChessGame(Player whitePlayer, Player blackPlayer) {
        this(whitePlayer, blackPlayer, new ArrayList<>(), Status.OPEN, new Date());
    }

    public ChessGame(Player whitePlayer, Player blackPlayer, List<ChessMove> moves, Status status, Date startDate) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.moves = moves;
        this.status = status;
        this.startDate = startDate;
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

    public List<ChessMove> getMoves() {
        return Collections.unmodifiableList(moves);
    }

    public Status getStatus() {
        return status;
    }

    public Date getStartDate() {
        return startDate;
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
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "id='" + id + '\'' +
                ", whitePlayer=" + whitePlayer +
                ", blackPlayer=" + blackPlayer +
                ", moves=" + moves +
                ", startDate=" + startDate +
                ", status=" + status +
                '}';
    }
}
