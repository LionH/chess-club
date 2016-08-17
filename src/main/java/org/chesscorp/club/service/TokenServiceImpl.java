package org.chesscorp.club.service;

import org.chesscorp.club.model.token.Token;
import org.chesscorp.club.model.token.TokenType;
import org.chesscorp.club.persistence.TokenRepository;
import org.chesscorp.club.utilities.token.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

/**
 * Implementation of low level Token management.
 */
@Component
public class TokenServiceImpl implements TokenService {

    private TokenRepository tokenRepository;

    private TokenGenerator tokenGenerator;

    @Autowired
    public TokenServiceImpl(TokenRepository tokenRepository, TokenGenerator tokenGenerator) {
        this.tokenRepository = tokenRepository;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    @Transactional
    public Token registerToken(TokenType tokenType, String systemIdentifier, int daysValidity) {
        String tokenString = tokenGenerator.generateToken();
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime exp = now.plusDays(daysValidity);
        Token token = new Token(
                tokenType, tokenString,
                now, exp,
                systemIdentifier);

        return tokenRepository.saveAndFlush(token);
    }

    @Override
    @Transactional
    public boolean validateToken(TokenType tokenType, String tokenText) {
        Token token = tokenRepository.getOneByTypeAndText(tokenType, tokenText);
        boolean result = token != null && token.getExpirationDate().isAfter(OffsetDateTime.now());

        if (result) {
            token.setUsages(token.getUsages() + 1);
            tokenRepository.saveAndFlush(token);
        }

        return result;
    }
}
