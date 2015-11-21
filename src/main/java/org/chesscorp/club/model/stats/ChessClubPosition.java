package org.chesscorp.club.model.stats;

import javax.persistence.*;

/**
 * Chess position reference.
 */
@Entity
@Table(name = "chessposition")
@SequenceGenerator(name = "chessposition_seq", initialValue = 1, allocationSize = 1, sequenceName = "chessposition_seq")
public class ChessClubPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chessposition_seq")
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
