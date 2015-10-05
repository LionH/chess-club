package org.chesscorp.club.service;

import org.chesscorp.club.model.Session;

/**
 * Created by yk on 19/07/15.
 */
public interface AuthenticationService {
    void signup(String email, String password, String displayName);

    String signin(String email, String password);

    Session getSession(String token);

    void revoke(String token);

}
