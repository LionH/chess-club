package org.chesscorp.club.exception;

/**
 * Created by yk on 18/07/15.
 */
public class AuthenticationFailedException extends AuthenticationException {

    public AuthenticationFailedException() {
        super("Authentication failed");
    }

    public AuthenticationFailedException(String message) {
        super(message);
    }
}
