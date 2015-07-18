package org.chesscorp.club.exception;

/**
 * Created by yk on 18/07/15.
 */
public class UserAlreadyExistsException extends AuthenticationException {

    public UserAlreadyExistsException() {
        super("User already exists");
    }

}
