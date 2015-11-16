package org.chesscorp.club.model.stats;

import javax.persistence.*;

/**
 * Chess position reference.
 */
@Entity
@Table(name = "CHESS_POSITION")
@SequenceGenerator(name = "CHESS_POSITION_SEQ", initialValue = 1, allocationSize = 1, sequenceName = "CHESS_POSITION_SEQ")
public class ChessClubPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHESS_POSITION_SEQ")
    private Long id;

    @Column(length = 70, unique = true)
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
