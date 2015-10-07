package org.chesscorp.club.service;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.model.ChessGame;
import org.chesscorp.club.model.ChessMove;
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

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@TransactionConfiguration(defaultRollback = true)
public class ChessGameServiceTest {
    private Logger logger = LoggerFactory.getLogger(ChessGameServiceTest.class);

    @Autowired
    private ChessGameService chessGameService;

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    @Transactional
    public void testGameCreation() {
        authenticationService.signup("a@b.c", "pwd", "Alcibiade");
        String alcibiadeToken = authenticationService.signin("a@b.c", "pwd");
        Player p1 = authenticationService.getSession(alcibiadeToken).getAccount().getPlayer();

        authenticationService.signup("b@b.c", "pwd", "Bob");
        String bobToken = authenticationService.signin("b@b.c", "pwd");
        Player p2 = authenticationService.getSession(bobToken).getAccount().getPlayer();

        ChessGame game = chessGameService.createGame(p1.getId(), p2.getId());
        Assertions.assertThat(game.getWhitePlayer()).isEqualToComparingFieldByField(p1);
        Assertions.assertThat(game.getBlackPlayer()).isEqualToComparingFieldByField(p2);
        Assertions.assertThat(game.getId().longValue()).isGreaterThan(0L);
        Assertions.assertThat(game.getStartDate()).isInThePast();

        game = chessGameService.move(game, "e2e4");
        Assertions.assertThat(game.getMoves()).extracting(ChessMove::getPgn).containsExactly("e4");

        game = chessGameService.move(game, "e7e5");
        Assertions.assertThat(game.getMoves()).extracting(ChessMove::getPgn).containsExactly("e4", "e5");

        Assertions.assertThat(chessGameService.searchGames(12345)).isEmpty();

        Assertions.assertThat(chessGameService.searchGames(p1.getId()).contains(game));
        Assertions.assertThat(chessGameService.searchGames(p2.getId()).contains(game));
    }

    @Test(expected = IllegalStateException.class)
    @Transactional
    public void testRefuseSamePlayer() {
        authenticationService.signup("a@b.c", "pwd", "Alcibiade");
        String alcibiadeToken = authenticationService.signin("a@b.c", "pwd");
        Player p1 = authenticationService.getSession(alcibiadeToken).getAccount().getPlayer();

        chessGameService.createGame(p1.getId(), p1.getId());
    }
}
