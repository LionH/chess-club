package org.chesscorp.club.model.stats;

import javax.persistence.*;

/**
 * Chess position reference.
 */
@Entity
public class ChessPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(length = 72, unique = true)
    private String text;


    public ChessPosition() {
    }

    public ChessPosition(String text) {
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
        return "ChessPosition{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
