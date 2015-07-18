package org.chesscorp.club.exception;

/**
 * Created by yk on 18/07/15.
 */
public abstract class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }
}
