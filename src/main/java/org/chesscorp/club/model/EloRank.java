package org.chesscorp.club.model;

import javax.persistence.*;

/**
 * Player data model. Can be used for actual players as well as robot players.
 *
 * @author Yannick Kirschhoffer <alcibiade@alcibiade.org>
 */
@Entity
public class EloRank {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    private Player player;

    @ManyToOne(optional = false)
    private ChessGame chessGame;

    @Column(nullable = false)
    private Integer elo;

    public EloRank() {
    }

    public EloRank(Player player, ChessGame chessGame, Integer elo) {
        this.player = player;
        this.chessGame = chessGame;
        this.elo = elo;
    }

    public Long getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public ChessGame getChessGame() {
        return chessGame;
    }

    public Integer getElo() {
        return elo;
    }

    @Override
    public String toString() {
        return "EloRank{" +
                "id=" + id +
                ", player=" + player +
                ", chessGame=" + chessGame +
                ", elo=" + elo +
                '}';
    }
}
