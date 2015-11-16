package org.chesscorp.club.model.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@SequenceGenerator(name = "CHESS_MOVE_SEQ", initialValue = 1, allocationSize = 1, sequenceName = "CHESS_MOVE_SEQ")
public class ChessMove {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHESS_MOVE_SEQ")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GAME_ID", nullable = false)
    @JsonIgnore
    private ChessGame game;
    @Column(nullable = false, length = 12)
    private String pgn;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public ChessMove() {
    }

    public ChessMove(ChessGame game, Date date, String pgn) {
        this.game = game;
        this.pgn = pgn;
        this.date = date;
    }

    public Long getId() {
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

    @Override
    public String toString() {
        return "ChessMove{" +
                "id=" + id +
                ", pgn='" + pgn + '\'' +
                ", date=" + date +
                '}';
    }
}
