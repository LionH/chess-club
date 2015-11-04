package org.chesscorp.club.model.game;

import org.alcibiade.chess.model.ChessGameStatus;
import org.chesscorp.club.model.people.Player;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.*;

/**
 * Chess game data model.
 *
 * @author Yannick Kirschhoffer <alcibiade@alcibiade.org>
 */
@Entity
@Proxy(lazy = false)
@Table(
        indexes = {
                @Index(columnList = "white_player", unique = false),
                @Index(columnList = "black_player", unique = false)
        }
)
public class ChessGame {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(optional = false)
    private Player whitePlayer;
    @ManyToOne(optional = false)
    private Player blackPlayer;
    @OneToMany(mappedBy = "game")
    private List<ChessMove> moves;
    @Column(nullable = false)
    private Date startDate;
    @Column(nullable = false)
    private ChessGameStatus status;

    // Extra PGN informations
    private String site;
    private String event;
    private Integer round;

    public ChessGame() {
    }

    public ChessGame(Player whitePlayer, Player blackPlayer) {
        this(whitePlayer, blackPlayer, new ArrayList<>(), ChessGameStatus.OPEN, new Date());
    }

    public ChessGame(Player whitePlayer, Player blackPlayer, List<ChessMove> moves, Date startDate,
                     ChessGameStatus status, String site, String event, Integer round) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.moves = moves;
        this.startDate = startDate;
        this.status = status;
        this.site = site;
        this.event = event;
        this.round = round;
    }

    public ChessGame(Player whitePlayer, Player blackPlayer, List<ChessMove> moves, ChessGameStatus status, Date startDate) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.moves = moves;
        this.status = status;
        this.startDate = startDate;
    }

    public ChessGame(ChessGame game, ChessMove move, ChessGameStatus status) {
        this.id = game.id;
        this.whitePlayer = game.whitePlayer;
        this.blackPlayer = game.blackPlayer;
        this.startDate = game.getStartDate();

        // Moves is a clone of the original list that is updated
        this.moves = new ArrayList<>(game.moves);
        this.moves.add(move);

        this.status = status;
    }

    public Long getId() {
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

    public ChessGameStatus getStatus() {
        return status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getSite() {
        return site;
    }

    public String getEvent() {
        return event;
    }

    public Integer getRound() {
        return round;
    }

    public Player getNextPlayer() {
        boolean whiteIsNext = moves.size() % 2 == 0;
        return whiteIsNext ? whitePlayer : blackPlayer;
    }

    public ChessMove addMove(Date moveDate, String movePgn) {
        ChessMove move = new ChessMove(this, moveDate, movePgn);
        this.moves.add(move);
        return move;
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
                ", movesCount=" + moves.size() +
                ", startDate=" + startDate +
                ", status=" + status +
                '}';
    }
}
