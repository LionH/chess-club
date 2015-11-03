package org.chesscorp.club.exception;

/**
 * Move validity issues are raised whenever a move is not possible on the current game position.
 */
public class InvalidChessMoveException extends ChessClubException {
    public InvalidChessMoveException(String move) {
        super("Invalid move: " + move);
    }

    public InvalidChessMoveException(String move, Throwable cause) {
        super("Invalid move: " + move, cause);
    }
}
