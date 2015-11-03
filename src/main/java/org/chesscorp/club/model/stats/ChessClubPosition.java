package org.chesscorp.club.model.stats;

import javax.persistence.*;

/**
 * Chess position reference.
 */
@Entity
public class ChessClubPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(length = 72, unique = true)
    private String text;


    public ChessClubPosition() {
    }

    public ChessClubPosition(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "ChessClubPosition{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
