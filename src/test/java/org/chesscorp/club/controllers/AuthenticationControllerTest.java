package org.chesscorp.club.controllers;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.dto.AuthenticationResult;
import org.chesscorp.club.exception.AuthenticationFailedException;
import org.chesscorp.club.exception.NotAuthenticatedException;
import org.chesscorp.club.exception.UserAlreadyExistsException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@TransactionConfiguration(defaultRollback = true)
public class AuthenticationControllerTest {
    private Logger logger = LoggerFactory.getLogger(AuthenticationControllerTest.class);

    @Autowired
    private AuthenticationController authenticationController;


    @Test
    @Transactional
    public void testSignUpAndSignIn() {
        authenticationController.signup("a@b.c", "Password1", "A");
        authenticationController.signin("a@b.c", "Password1");
    }

    @Test
    @Transactional
    public void testSignUpAndSignOut() {
        AuthenticationResult auth = authenticationController.signup("a@b.c", "Password1", "A");
        authenticationController.signout(auth.getToken());
    }

    @Test(expected = AuthenticationFailedException.class)
    @Transactional
    public void testUserNotFound() {
        authenticationController.signin("a@b.c", "Password1");
    }

    @Test(expected = UserAlreadyExistsException.class)
    @Transactional
    public void testUserAlreadyExisting() {
        authenticationController.signup("a@b.c", "Password1", "A");
        authenticationController.signup("a@b.c", "Password1", "A");
    }

    @Test(expected = NotAuthenticatedException.class)
    @Transactional
    public void testCredentialsUnauthenticated() {
        authenticationController.getCredentials("TT");
    }

    @Test
    @Transactional
    public void testCredentialsAuthenticated() {
        AuthenticationResult auth = authenticationController.signup("a@b.c", "Password1", "A");
        AuthenticationResult credentials = authenticationController.getCredentials(auth.getToken());
        Assertions.assertThat(credentials).isEqualToComparingFieldByField(auth);
    }
}
