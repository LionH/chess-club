package org.chesscorp.club.dto;

import org.chesscorp.club.model.Account;
import org.chesscorp.club.model.Player;

/**
 * Returned by the server on successful authentication.
 */
public class AuthenticationResult {

    private String token;

    private String accountIdentifier;

    private Player player;

    public AuthenticationResult() {
    }

    public AuthenticationResult(String token, Account account, Player player) {
        this.token = token;
        this.accountIdentifier = account.getIdentifier();
        this.player = player;
    }

    public String getToken() {
        return token;
    }

    public String getAccountIdentifier() {
        return accountIdentifier;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "AuthenticationResult{" +
                "token='" + token + '\'' +
                ", accountIdentifier='" + accountIdentifier + '\'' +
                ", player=" + player +
                '}';
    }
}
