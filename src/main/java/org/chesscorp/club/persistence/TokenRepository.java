package org.chesscorp.club.persistence;

import org.chesscorp.club.model.token.Token;
import org.chesscorp.club.model.token.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Token getOneByTypeAndText(TokenType tokenType, String text);

}
