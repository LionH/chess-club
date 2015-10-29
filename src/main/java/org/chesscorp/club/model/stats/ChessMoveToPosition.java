package org.chesscorp.club.model.stats;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Chess position reference.
 */
@Entity
public class ChessMoveToPosition {

    @Id
    private Long chessMoveId;

    @ManyToOne
    private ChessPosition chessPosition;

    public ChessMoveToPosition() {
    }

    public ChessMoveToPosition(Long chessMoveId, ChessPosition chessPosition) {
        this.chessMoveId = chessMoveId;
        this.chessPosition = chessPosition;
    }

    public Long getChessMoveId() {
        return chessMoveId;
    }

    public ChessPosition getChessPosition() {
        return chessPosition;
    }

    @Override
    public String toString() {
        return "ChessMoveToPosition{" +
                "chessMoveId=" + chessMoveId +
                ", chessPosition=" + chessPosition +
                '}';
    }
}
