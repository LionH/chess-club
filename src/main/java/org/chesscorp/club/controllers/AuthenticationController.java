package org.chesscorp.club.controllers;

import org.chesscorp.club.dto.AuthenticationRequest;
import org.chesscorp.club.dto.AuthenticationResult;
import org.chesscorp.club.dto.SubscriptionRequest;
import org.chesscorp.club.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Yannick Kirschhoffer alcibiade@alcibiade.org
 */
@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {
    private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @Transactional
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public AuthenticationResult signup(
            @RequestBody SubscriptionRequest subscriptionRequest,
            HttpServletResponse response) {
        authenticationService.signup(
                subscriptionRequest.getEmail(),
                subscriptionRequest.getPassword(),
                subscriptionRequest.getDisplayName()
        );

        String token = authenticationService.signin(
                subscriptionRequest.getEmail(),
                subscriptionRequest.getPassword());

        response.addCookie(new Cookie("AUTH_TOKEN", token));

        return new AuthenticationResult(token);
    }

    @Transactional
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public AuthenticationResult signin(
            @RequestBody AuthenticationRequest authenticationRequest,
            HttpServletResponse response) {

        String token = authenticationService.signin(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword());

        response.addCookie(new Cookie("AUTH_TOKEN", token));

        return new AuthenticationResult(token);
    }

    @Transactional
    @RequestMapping(value = "/signout", method = RequestMethod.POST)
    public void signout(@CookieValue("AUTH_TOKEN") String token) {
        authenticationService.revoke(token);
    }
}
