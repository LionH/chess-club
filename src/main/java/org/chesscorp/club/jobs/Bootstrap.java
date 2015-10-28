package org.chesscorp.club.jobs;


import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.people.Account;
import org.chesscorp.club.model.people.ClubPlayer;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.model.people.RobotPlayer;
import org.chesscorp.club.persistence.AccountRepository;
import org.chesscorp.club.persistence.ChessGameRepository;
import org.chesscorp.club.persistence.PlayerRepository;
import org.chesscorp.club.persistence.RobotRepository;
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
    private RobotRepository robotRepository;

    @Autowired
    private ChessGameRepository chessGameRepository;

    @Autowired
    private AccountRepository accountRepository;

    @PostConstruct
    public void populate() {
        long playerCount = playerRepository.count();
        long gameCount = chessGameRepository.count();
        long accountCount = accountRepository.count();

        logger.info("Found {} accounts, {} players, {} games", accountCount, playerCount, gameCount);

        if (playerCount == 0 && gameCount == 0 && accountCount == 0) {
            logger.info("Creating sample robots");
            robotRepository.save(new RobotPlayer("Simple AI", "randomAI", ""));

            logger.info("Creating sample players");
            Player alcibiade = playerRepository.save(new ClubPlayer("Alcibiade"));
            Player john = playerRepository.save(new ClubPlayer("John"));
            Player bob = playerRepository.save(new ClubPlayer("Bob"));
            playerRepository.save(new ClubPlayer("Steve"));

            logger.info("Creating sample games");
            chessGameRepository.save(new ChessGame(playerRepository.getOne(john.getId()), playerRepository.getOne(bob.getId())));
            chessGameRepository.save(new ChessGame(playerRepository.getOne(alcibiade.getId()), playerRepository.getOne(bob.getId())));

            logger.info("Creating sample accounts");
            accountRepository.save(new Account("alcibiade", "toto", alcibiade));
            accountRepository.save(new Account("john", "toto", john));
        }
    }
}
