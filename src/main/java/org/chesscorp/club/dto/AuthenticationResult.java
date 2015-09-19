package org.chesscorp.club.dto;

/**
 * Returned by the server on successful authentication.
 */
public class AuthenticationResult {

    private String token;

    public AuthenticationResult(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "AuthenticationResult{" + "token='" + token + '\'' + '}';
    }
}
