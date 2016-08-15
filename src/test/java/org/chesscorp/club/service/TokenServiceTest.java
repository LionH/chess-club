package org.chesscorp.club.service;


import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
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
        tokenService.registerToken(TokenType.ACCOUNT_VALIDATION, "SYS1", 30);
        Assertions.assertThat(tokenRepository.findAll()).hasSize(1);
    }
}
