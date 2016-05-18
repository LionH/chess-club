package org.chesscorp.club.model.game;

import org.chesscorp.club.model.people.Player;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * Player data model. Can be used for actual players as well as robot players.
 *
 * @author Yannick Kirschhoffer <alcibiade@alcibiade.org>
 */
@Entity
@SequenceGenerator(name = "elorating_seq", initialValue = 1, allocationSize = 1, sequenceName = "elorating_seq")
public class EloRating implements Comparable<EloRating> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "elorating_seq")
    private Long id;

    @Column(nullable = false)
    private OffsetDateTime scoreDate;

    @ManyToOne(optional = false)
    private Player player;

    @ManyToOne(optional = false)
    private ChessGame chessGame;

    @Column(nullable = false)
    private Integer eloRating;

    public EloRating() {
    }

    public EloRating(Player player, ChessGame chessGame, Integer eloRating) {
        this.player = player;
        this.chessGame = chessGame;
        this.eloRating = eloRating;
        this.scoreDate = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public OffsetDateTime getScoreDate() {
        return scoreDate;
    }

    public Player getPlayer() {
        return player;
    }

    public ChessGame getChessGame() {
        return chessGame;
    }

    public Integer getEloRating() {
        return eloRating;
    }

    @Override
    public String toString() {
        return "EloRating{" +
                "id=" + id +
                ", player=" + player +
                ", chessGame=" + chessGame +
                ", eloRating=" + eloRating +
                '}';
    }

    @Override
    public int compareTo(EloRating o) {
        int result = scoreDate.compareTo(o.scoreDate);

        if (result == 0) {
            result = id.compareTo(o.id);
        }

        return result;
    }
}
