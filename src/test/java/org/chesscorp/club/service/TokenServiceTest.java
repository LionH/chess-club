package org.chesscorp.club.service;


import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.model.token.Token;
import org.chesscorp.club.model.token.TokenType;
import org.chesscorp.club.persistence.TokenRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TokenRepository tokenRepository;

    @Test
    @Transactional
    public void testTokenGeneration() {
        Token token = tokenService.registerToken(TokenType.ACCOUNT_VALIDATION, "SYS1", 30);
        Assertions.assertThat(tokenRepository.findAll()).hasSize(1);
        Assertions.assertThat(token.getIssueDate()).isBefore(token.getExpirationDate());
        Assertions.assertThat(token.getText()).isNotEmpty();
    }

    @Test
    @Transactional
    public void testTokenUnicity() {
        Token token1 = tokenService.registerToken(TokenType.ACCOUNT_VALIDATION, "SYS1", 30);
        Token token2 = tokenService.registerToken(TokenType.ACCOUNT_VALIDATION, "SYS1", 30);
        Assertions.assertThat(tokenRepository.findAll()).hasSize(2);

        Assertions.assertThat(token1.getText()).isNotEmpty().isNotEqualTo(token2.getText());
    }

    @Test
    @Transactional
    public void testTokenUsage() {
        Token token1 = tokenService.registerToken(TokenType.ACCOUNT_VALIDATION, "SYS1", 30);
        Token token2 = tokenService.registerToken(TokenType.ACCOUNT_VALIDATION, "SYS1", 30);

        Assertions.assertThat(tokenService.validateToken(TokenType.ACCOUNT_VALIDATION, "Hello")).isNull();
        Assertions.assertThat(tokenService.validateToken(TokenType.ACCOUNT_VALIDATION, token1.getText())).isNotNull();
        Assertions.assertThat(tokenRepository.getOne(token1.getId()).getUsages()).isEqualTo(1);
        Assertions.assertThat(tokenRepository.getOne(token2.getId()).getUsages()).isEqualTo(0);
    }
}
