package org.chesscorp.club.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Base class for authentication/authorization exceptions.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public abstract class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }
}
