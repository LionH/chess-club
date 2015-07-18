package org.chesscorp.club.controllers;

import org.chesscorp.club.exception.AuthenticationFailedException;
import org.chesscorp.club.exception.UserAlreadyExistsException;
import org.chesscorp.club.model.Account;
import org.chesscorp.club.persistence.AccountRepository;
import org.chesscorp.club.persistence.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
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
    private AccountRepository accountRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Transactional
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public void signup(
            @RequestParam String email,
            @RequestParam String password) {
        if (accountRepository.exists(email)) {
            throw new UserAlreadyExistsException();
        }

        Account account = accountRepository.save(new Account(email, password));
        logger.info("Account created for {}");
    }

    @Transactional
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public void signin(
            @RequestParam String email,
            @RequestParam String password) {
        Account account;

        try {
            account = accountRepository.getOne(email);
        } catch (JpaObjectRetrievalFailureException e) {
            account = null;
        }

        if (account == null || !account.getPassword().equals(password)) {
            throw new AuthenticationFailedException();
        }
    }
}
