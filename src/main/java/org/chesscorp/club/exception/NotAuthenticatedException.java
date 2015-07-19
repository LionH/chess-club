package org.chesscorp.club.exception;

/**
 * Created by yk on 18/07/15.
 */
public class NotAuthenticatedException extends AuthenticationException {

    public NotAuthenticatedException() {
        super("Not authenticated");
    }
}
