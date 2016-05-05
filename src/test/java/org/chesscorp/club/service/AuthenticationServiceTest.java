package org.chesscorp.club.service;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.exception.AuthenticationFailedException;
import org.chesscorp.club.exception.NotAuthenticatedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class AuthenticationServiceTest {
    private Logger logger = LoggerFactory.getLogger(AuthenticationServiceTest.class);

    @Autowired
    private AuthenticationService authenticationService;

    @Test(expected = NotAuthenticatedException.class)
    @Transactional
    public void testRegistration() {
        authenticationService.signup("alcibiade@alcibiade.org", "password", "Alcibiade");
        String token = authenticationService.signin("alcibiade@alcibiade.org", "password");

        authenticationService.getSession(token);
        authenticationService.revoke(token);
        authenticationService.getSession(token);
    }

    @Test
    @Transactional
    public void testPasswordUpdate() {
        authenticationService.signup("alcibiade@alcibiade.org", "password", "Alcibiade");
        authenticationService.signin("alcibiade@alcibiade.org", "password");

        authenticationService.updatePassword("alcibiade@alcibiade.org", "password", "anotherPassword");

        try {
            authenticationService.signin("alcibiade@alcibiade.org", "password");
            Assertions.fail("Authentication should have failed with the original password");
        } catch (AuthenticationFailedException e) {
            // We expect to have this exception raised.
        }

        authenticationService.signin("alcibiade@alcibiade.org", "anotherPassword");
    }
}
