package org.chesscorp.club.exception;

/**
 * Created by yk on 20/07/15.
 */
public class InvalidChessMoveException extends ChessException {
    public InvalidChessMoveException(String move) {
        super("Invalid move: " + move);
    }

    public InvalidChessMoveException(String move, Throwable cause) {
        super("Invalid move: " + move, cause);
    }
}
