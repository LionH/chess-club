package org.chesscorp.club.service;

import org.chesscorp.club.exception.AuthenticationFailedException;
import org.chesscorp.club.exception.NotAuthenticatedException;
import org.chesscorp.club.exception.UserAlreadyExistsException;
import org.chesscorp.club.model.people.Account;
import org.chesscorp.club.model.people.ClubPlayer;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.model.people.Session;
import org.chesscorp.club.model.token.Token;
import org.chesscorp.club.model.token.TokenType;
import org.chesscorp.club.persistence.AccountRepository;
import org.chesscorp.club.persistence.PlayerRepository;
import org.chesscorp.club.persistence.SessionRepository;
import org.chesscorp.club.utilities.hash.GravatarHashManager;
import org.chesscorp.club.utilities.hash.HashManager;
import org.chesscorp.club.utilities.token.TokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

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

    @Autowired
    private HashManager hashManager;

    @Autowired
    private GravatarHashManager gravatarHashManager;

    @Autowired
    private MailService mailService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TokenGenerator tokenGenerator;

    @PostConstruct
    private void hashClearTextPasswords() {
        accountRepository.readAllBySaltNull().forEach(account -> {
            String salt = hashManager.createSalt();
            String password = account.getPassword();

            String passwordHash = hashManager.hash(salt, password);

            account.setSalt(salt);
            account.setPassword(passwordHash);
            accountRepository.save(account);
        });
    }

    @Override
    @Transactional
    public void signup(String email, String password, String displayName) {
        if (accountRepository.exists(email)) {
            throw new UserAlreadyExistsException();
        }

        String salt = hashManager.createSalt();
        String passwordHash = hashManager.hash(salt, password);

        String avatarHash = gravatarHashManager.hashGravatar(email);
        Player p = playerRepository.save(new ClubPlayer(displayName, avatarHash));
        Account a = accountRepository.save(new Account(email, salt, passwordHash, p));
        logger.info("Account {} created for player {}", a, p);

        Token token = tokenService.registerToken(TokenType.ACCOUNT_VALIDATION, a.getIdentifier(), 30);
        mailService.sendAccountValidationLink(displayName, email, token.getText());
    }

    @Override
    @Transactional
    public String signin(String email, String password) {
        Account account = getAccount(email, password);

        String token = tokenGenerator.generateToken();
        sessionRepository.save(new Session(token, account));

        return token;
    }

    @Override
    @Transactional
    public void updatePassword(String email, String previousPassword, String newPassword) {
        Account account = getAccount(email, previousPassword);

        String salt = hashManager.createSalt();
        String passwordHash = hashManager.hash(salt, newPassword);

        account.setSalt(salt);
        account.setPassword(passwordHash);

        accountRepository.saveAndFlush(account);
    }

    @Override
    @Transactional(readOnly = true)
    public Session getSession(String token) {
        try {
            Session session = sessionRepository.getOne(token);
            return session;
        } catch (JpaObjectRetrievalFailureException e) {
            throw new NotAuthenticatedException();
        }
    }

    @Override
    @Transactional
    public void revoke(String token) {
        sessionRepository.delete(token);
    }

    private Account getAccount(String email, String password) {
        Account account = accountRepository.findOne(email);

        if (account == null) {
            throw new AuthenticationFailedException("No account for '" + email + "'");
        }

        String passwordHash = hashManager.hash(account.getSalt(), password);

        if (!account.getPassword().equals(passwordHash)) {
            throw new AuthenticationFailedException("Invalid password");
        }

        return account;
    }
}
