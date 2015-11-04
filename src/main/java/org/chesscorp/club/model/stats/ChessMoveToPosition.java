package org.chesscorp.club.model.stats;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Chess position reference.
 */
@Entity
public class ChessMoveToPosition {

    @Id
    private Long chessMoveId;

    @ManyToOne(fetch = FetchType.EAGER)
    private ChessClubPosition chessClubPosition;

    public ChessMoveToPosition() {
    }

    public ChessMoveToPosition(Long chessMoveId, ChessClubPosition chessClubPosition) {
        this.chessMoveId = chessMoveId;
        this.chessClubPosition = chessClubPosition;
    }

    public Long getChessMoveId() {
        return chessMoveId;
    }

    public ChessClubPosition getChessPosition() {
        return chessClubPosition;
    }

    @Override
    public String toString() {
        return "ChessMoveToPosition{" +
                "chessMoveId=" + chessMoveId +
                ", chessPosition=" + chessClubPosition +
                '}';
    }
}
