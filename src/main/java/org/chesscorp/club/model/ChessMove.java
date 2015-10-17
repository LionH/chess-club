package org.chesscorp.club.model;

import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Date;

/**
 * Chess game single move.
 *
 * @author Yannick Kirschhoffer <alcibiade@alcibiade.org>
 */
@Entity
@Proxy(lazy = false)
public class ChessMove {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GAME_ID")
    private ChessGame game;

    private String pgn;

    private Date date;

    public ChessMove() {
    }

    public ChessMove(String pgn) {
        this.pgn = pgn;
        this.date = new Date();
    }

    public Number getId() {
        return id;
    }

    public String getPgn() {
        return pgn;
    }

    public Date getDate() {
        return date;
    }

    public ChessGame getGame() {
        return game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChessMove chessMove = (ChessMove) o;

        return id.equals(chessMove.id) && pgn.equals(chessMove.pgn) && date.equals(chessMove.date);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + pgn.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }
}
