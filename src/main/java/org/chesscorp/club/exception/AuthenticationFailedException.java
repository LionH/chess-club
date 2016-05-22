package org.chesscorp.club.exception;

/**
 * General exception for authentication failure.
 */
public class AuthenticationFailedException extends AuthenticationException {

    public AuthenticationFailedException() {
        super("Authentication failed");
    }

    public AuthenticationFailedException(String message) {
        super(message);
    }
}
