package org.chesscorp.club.service;

import org.chesscorp.club.model.Player;

/**
 * Created by yk on 19/07/15.
 */
public interface AuthenticationService {
    void signup(String email, String password, String displayName);

    String signin(String email, String password);

    Player getPlayer(String token);

    void revoke(String token);

}
