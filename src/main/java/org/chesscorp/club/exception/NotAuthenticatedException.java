package org.chesscorp.club.exception;

/**
 * Exception thrown when an operation requiring an authentication os called out of a valid session.
 *
 * @author Yannick Kirschhoffer
 */
public class NotAuthenticatedException extends AuthenticationException {

    public NotAuthenticatedException() {
        super("Not authenticated");
    }

    public NotAuthenticatedException(String message) {
        super(message);
    }
}
