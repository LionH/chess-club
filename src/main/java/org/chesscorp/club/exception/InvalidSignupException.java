package org.chesscorp.club.exception;

/**
 * User information clash.
 */
public class InvalidSignupException extends AuthenticationException {

    public InvalidSignupException(String message) {
        super(message);
    }

}
