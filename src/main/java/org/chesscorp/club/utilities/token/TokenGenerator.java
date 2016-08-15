package org.chesscorp.club.utilities.token;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Token generation utility.
 */
@Component
public class TokenGenerator {

    public String generateToken() {
        String token = UUID.randomUUID().toString();
        return token;
    }
}
