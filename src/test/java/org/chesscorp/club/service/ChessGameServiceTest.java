package org.chesscorp.club.service;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.model.ChessGame;
import org.chesscorp.club.model.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@TransactionConfiguration(defaultRollback = true)
public class ChessGameServiceTest {
    private Logger logger = LoggerFactory.getLogger(ChessGameServiceTest.class);

    @Autowired
    private ChessGameService chessGameService;

    @Autowired
    private PlayerService playerService;

    @Test
    @Transactional
    public void testGameCreation() {
        Player p1 = new Player("yannick", "Alcibiade");
        Player p2 = new Player("anatoli", "Chessmaster");
        playerService.register(p1);
        playerService.register(p2);

        ChessGame game = chessGameService.createGame(p1.getId(), p2.getId());
        Assertions.assertThat(game.getWhitePlayer()).isEqualToComparingFieldByField(p1);
        Assertions.assertThat(game.getBlackPlayer()).isEqualToComparingFieldByField(p2);
        Assertions.assertThat(game.getId()).isNotEmpty();
        Assertions.assertThat(game.getStartDate()).isInThePast();
    }

}
