package org.chesscorp.club.exception;

/**
 * User information clash.
 */
public class UserAlreadyExistsException extends AuthenticationException {

    public UserAlreadyExistsException() {
        super("User already exists");
    }

}
