package org.chesscorp.club.controllers;

import org.chesscorp.club.Application;
import org.chesscorp.club.dto.AuthenticationRequest;
import org.chesscorp.club.dto.SubscriptionRequest;
import org.chesscorp.club.exception.AuthenticationFailedException;
import org.chesscorp.club.exception.UserAlreadyExistsException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        authenticationController.signup(new SubscriptionRequest("a@b.c", "Password1", "A"), response);
        authenticationController.signin(new AuthenticationRequest("a@b.c", "Password1"), response);
        Mockito.verify(response, Mockito.times(2)).addCookie(Mockito.any(Cookie.class));
    }

    @Test(expected = AuthenticationFailedException.class)
    @Transactional
    public void testUserNotFound() {
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        authenticationController.signin(new AuthenticationRequest("a@b.c", "Password1"), response);
        Mockito.verify(response, Mockito.times(1)).addCookie(Mockito.any(Cookie.class));
    }

    @Test(expected = UserAlreadyExistsException.class)
    @Transactional
    public void testUserAlreadyExisting() {
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        authenticationController.signup(new SubscriptionRequest("a@b.c", "Password1", "A"), response);
        authenticationController.signup(new SubscriptionRequest("a@b.c", "Password1", "A"), response);
    }

}
