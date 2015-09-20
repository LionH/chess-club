package org.chesscorp.club.service;

import org.chesscorp.club.exception.AuthenticationFailedException;
import org.chesscorp.club.exception.NotAuthenticatedException;
import org.chesscorp.club.exception.UserAlreadyExistsException;
import org.chesscorp.club.model.Account;
import org.chesscorp.club.model.Player;
import org.chesscorp.club.model.Session;
import org.chesscorp.club.persistence.AccountRepository;
import org.chesscorp.club.persistence.PlayerRepository;
import org.chesscorp.club.persistence.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Authentication mechanisms based on account/player repositories.
 */
@Component
public class AuthenticationServiceImpl implements AuthenticationService {
    private Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Override
    @Transactional
    public void signup(String email, String password, String displayName) {
        if (accountRepository.exists(email)) {
            throw new UserAlreadyExistsException();
        }

        Player p = playerRepository.save(new Player(displayName));
        Account a = accountRepository.save(new Account(email, password, p));
        logger.info("Account {} created for player {}", a, p);
    }

    @Override
    @Transactional
    public String signin(String email, String password) {
        Account account;

        try {
            account = accountRepository.getOne(email);
        } catch (JpaObjectRetrievalFailureException e) {
            account = null;
        }

        if (account == null || !account.getPassword().equals(password)) {
            throw new AuthenticationFailedException();
        }

        String token = UUID.randomUUID().toString();
        sessionRepository.save(new Session(token, account));
        return token;
    }

    @Override
    @Transactional(readOnly = true)
    public Player getPlayer(String token) {
        try {
            Session session = sessionRepository.getOne(token);
            Account account = session.getAccount();
            return account.getPlayer();
        } catch (JpaObjectRetrievalFailureException e) {
            throw new NotAuthenticatedException();
        }
    }

    @Override
    @Transactional
    public void revoke(String token) {
        sessionRepository.delete(token);
    }
}
