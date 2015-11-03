package org.chesscorp.club.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Base class for chess-related functional failures.
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ChessClubException extends RuntimeException {
    public ChessClubException(String message) {
        super(message);
    }

    public ChessClubException(String message, Throwable cause) {
        super(message, cause);
    }
}
