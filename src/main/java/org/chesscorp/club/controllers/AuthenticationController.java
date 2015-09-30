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
    public AuthenticationResult signup(@RequestBody SubscriptionRequest subscriptionRequest) {
        authenticationService.signup(
                subscriptionRequest.getEmail(),
                subscriptionRequest.getPassword(),
                subscriptionRequest.getDisplayName()
        );

        String token = authenticationService.signin(
                subscriptionRequest.getEmail(),
                subscriptionRequest.getPassword());

        return new AuthenticationResult(token);
    }

    @Transactional
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public AuthenticationResult signin(@RequestBody AuthenticationRequest authenticationRequest) {

        String token = authenticationService.signin(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword());

        return new AuthenticationResult(token);
    }

    @Transactional
    @RequestMapping(value = "/signout", method = RequestMethod.POST)
    public void signout(@CookieValue(value = "AUTH_TOKEN", required = false) String token) {
        if (token == null) {
            logger.debug("No token found on sign out.");
        } else {
            authenticationService.revoke(token);
        }
    }

}
