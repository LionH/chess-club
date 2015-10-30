package org.chesscorp.club.model.comment;

import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.people.Player;

import javax.persistence.*;
import java.util.Date;

/**
 * Comment related to a chess game.
 */
@Entity
public class ChessComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    private Player author;

    @ManyToOne(optional = false)
    private ChessGame chessGame;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(length = 4096, nullable = false)
    private String text;

    @Transient
    private String html;

    public ChessComment() {
    }

    public ChessComment(Player author, ChessGame chessGame, Date date, String text) {
        this.author = author;
        this.chessGame = chessGame;
        this.date = date;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public Player getAuthor() {
        return author;
    }

    public ChessGame getChessGame() {
        return chessGame;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    @Override
    public String toString() {
        return "ChessComment{" +
                "id=" + id +
                ", author=" + author +
                ", date=" + date +
                ", chessGame=" + chessGame +
                '}';
    }
}
