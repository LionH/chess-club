package org.chesscorp.club.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

/**
 * Create cookies for authentication.
 */
@Component
public class CookieFactory {

    @Value("${auth.cookie.domain:localhost}")
    private String cookieDomain;

    /**
     * Cookie factory method.
     *
     * @param token an authentication token
     * @return an HTTP cookie
     */
    public Cookie createCookie(String token) {
        Cookie cookie = new Cookie("AUTH_TOKEN", token);
        cookie.setDomain(cookieDomain);
        return cookie;
    }

}
