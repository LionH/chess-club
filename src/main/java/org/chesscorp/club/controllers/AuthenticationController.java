package org.chesscorp.club.controllers;

import org.chesscorp.club.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public void signup(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String displayName) {
        authenticationService.signup(email, password, displayName);
    }

    @Transactional
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public String signin(
            @RequestParam String email,
            @RequestParam String password) {
        return authenticationService.signin(email, password);
    }
}
