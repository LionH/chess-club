package org.chesscorp.club.jobs;


import org.chesscorp.club.model.Account;
import org.chesscorp.club.model.ChessGame;
import org.chesscorp.club.model.Player;
import org.chesscorp.club.persistence.AccountRepository;
import org.chesscorp.club.persistence.ChessGameRepository;
import org.chesscorp.club.persistence.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Profile("bootstrap")
public class Bootstrap {

    private Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ChessGameRepository chessGameRepository;

    @Autowired
    private AccountRepository accountRepository;

    @PostConstruct
    public void populate() {
        if (playerRepository.count() == 0) {
            logger.info("Creating sample players");
            playerRepository.save(new Player("alcibiade", "Alcibiade"));
            playerRepository.save(new Player("john", "John"));
            playerRepository.save(new Player("bob", "Bob"));
            playerRepository.save(new Player("steve", "Steve"));
        }

        if (chessGameRepository.count() == 0) {
            logger.info("Creating sample games");
            chessGameRepository.save(new ChessGame(playerRepository.getOne("john"), playerRepository.getOne("bob")));
            chessGameRepository.save(new ChessGame(playerRepository.getOne("alcibiade"), playerRepository.getOne("bob")));
        }

        if (accountRepository.count() == 0) {
            logger.info("Creating sample accounts");
            accountRepository.save(new Account("alcibiade", "toto"));
            accountRepository.save(new Account("john", "toto"));
        }
    }
}
