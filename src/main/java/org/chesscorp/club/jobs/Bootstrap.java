package org.chesscorp.club.jobs;


import org.chesscorp.club.model.ChessGame;
import org.chesscorp.club.model.Player;
import org.chesscorp.club.persistence.ChessGameRepository;
import org.chesscorp.club.persistence.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Bootstrap {

    private Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ChessGameRepository chessGameRepository;

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
    }
}
