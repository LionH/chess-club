package org.chesscorp.club.service;

import org.chesscorp.club.model.token.Token;
import org.chesscorp.club.model.token.TokenType;

/**
 * Token management.
 */
public interface TokenService {

    Token registerToken(TokenType tokenType, String systemIdentifier, int daysValidity);

    Token validateToken(TokenType tokenType, String tokenText);
}
