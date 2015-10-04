package org.chesscorp.club.dto;

import org.chesscorp.club.model.Player;

/**
 * Returned by the server on successful authentication.
 */
public class AuthenticationResult {

    private String token;

    private Player player;

    public AuthenticationResult(String token, Player player) {
        this.token = token;
        this.player = player;
    }

    public String getToken() {
        return token;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "AuthenticationResult{" + "token='" + token + '\'' + ", player=" + player + '}';
    }
}
