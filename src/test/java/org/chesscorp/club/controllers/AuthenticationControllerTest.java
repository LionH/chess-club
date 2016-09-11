package org.chesscorp.club.controllers;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.dto.AuthenticationResult;
import org.chesscorp.club.exception.AuthenticationFailedException;
import org.chesscorp.club.exception.InvalidSignupException;
import org.chesscorp.club.exception.UserAlreadyExistsException;
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
public class AuthenticationControllerTest {
    private Logger logger = LoggerFactory.getLogger(AuthenticationControllerTest.class);

    @Autowired
    private AuthenticationController authenticationController;

    @Test
    @Transactional
    public void testSignUpAndSignIn() {
        authenticationController.signup("a@b.com", "Password1", "A");
        authenticationController.signin("a@b.com", "Password1");
    }

    @Transactional
    @Test(expected = InvalidSignupException.class)
    public void testInvalidSignUpAndSignIn() {
        authenticationController.signup("a@b.", "Password1", "A");
        authenticationController.signup("a@b.c", "Password1", "B");
    }

    @Test
    @Transactional
    public void testChangePassword() {
        authenticationController.signup("a@b.com", "Password1", "A");
        AuthenticationResult authenticationResult = authenticationController.signin("a@b.com", "Password1");
        authenticationController.updatePassword(authenticationResult.getToken(), "Password1", "Password2");
        authenticationController.signin("a@b.com", "Password2");
    }

    @Test
    @Transactional
    public void testSignUpAndSignOut() {
        AuthenticationResult auth = authenticationController.signup("a@b.com", "Password1", "A");
        authenticationController.signout(auth.getToken());
    }

    @Test(expected = AuthenticationFailedException.class)
    @Transactional
    public void testUserNotFound() {
        authenticationController.signin("a@b.com", "Password1");
    }

    @Test(expected = UserAlreadyExistsException.class)
    @Transactional
    public void testUserAlreadyExisting() {
        authenticationController.signup("a@b.com", "Password1", "A");
        authenticationController.signup("a@b.com", "Password1", "A");
    }

    @Test
    @Transactional
    public void testCredentialsUnauthenticated() {
        AuthenticationResult authentication = authenticationController.getCredentials("TT");
        Assertions.assertThat(authentication.getToken()).isNull();
        Assertions.assertThat(authentication.getPlayer()).isNull();
    }

    @Test
    @Transactional
    public void testCredentialsAuthenticated() {
        AuthenticationResult auth = authenticationController.signup("a@b.com", "Password1", "A");
        // authenticationController.validateAccount();

        AuthenticationResult credentials = authenticationController.getCredentials(auth.getToken());
        Assertions.assertThat(credentials).isEqualToComparingFieldByField(auth);
    }
}
