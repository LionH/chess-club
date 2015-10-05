package org.chesscorp.club.controllers;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.exception.ChessException;
import org.chesscorp.club.model.ChessGame;
import org.chesscorp.club.model.ChessMove;
import org.chesscorp.club.model.Player;
import org.chesscorp.club.persistence.PlayerRepository;
import org.chesscorp.club.service.AuthenticationService;
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
public class ChessGameControllerTest {
    private Logger logger = LoggerFactory.getLogger(ChessGameControllerTest.class);

    @Autowired
    private ChessGameController chessGameController;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    @Transactional
    public void testBasicGame() {
        authenticationService.signup("a@b.c", "pwd", "Alcibiade");
        String alcibiadeToken = authenticationService.signin("a@b.c", "pwd");
        Player alcibiade = authenticationService.getSession(alcibiadeToken).getAccount().getPlayer();


        authenticationService.signup("b@b.c", "pwd", "Bob");
        String bobToken = authenticationService.signin("b@b.c", "pwd");
        Player bob = authenticationService.getSession(bobToken).getAccount().getPlayer();

        /*
         * Game creation.
         */

        ChessGame game1 = chessGameController.createGame(alcibiadeToken, alcibiade.getId(), bob.getId());
        Assertions.assertThat(game1.getWhitePlayer()).isEqualToComparingFieldByField(alcibiade);
        Assertions.assertThat(game1.getBlackPlayer()).isEqualToComparingFieldByField(bob);
        Assertions.assertThat(game1.getId().longValue()).isGreaterThan(0L);
        Assertions.assertThat(game1.getStartDate()).isInThePast();

        /*
         * Game fetch
         */

        ChessGame game2 = chessGameController.getGame(game1.getId());
        Assertions.assertThat(game2).isEqualToComparingFieldByField(game1);

        /*
         * Game moves
         */

        ChessGame game3 = chessGameController.postMove(game1.getId(), alcibiadeToken, "e4");
        Assertions.assertThat(game3.getMoves()).extracting(ChessMove::getPgn).containsExactly("e4");

        ChessGame game4 = chessGameController.postMove(game1.getId(), bobToken, "e5");
        Assertions.assertThat(game4.getMoves()).extracting(ChessMove::getPgn).containsExactly("e4", "e5");
    }

    @Test(expected = ChessException.class)
    @Transactional
    public void testRefuseThirdPartyCreation() {
        authenticationService.signup("a@b.c", "pwd", "Alcibiade");
        String alcibiadeToken = authenticationService.signin("a@b.c", "pwd");
        Player alcibiade = authenticationService.getSession(alcibiadeToken).getAccount().getPlayer();

        authenticationService.signup("b@b.c", "pwd", "Bob");
        String bobToken = authenticationService.signin("b@b.c", "pwd");
        Player bob = authenticationService.getSession(bobToken).getAccount().getPlayer();

        authenticationService.signup("c@b.c", "pwd", "Charlie");
        String charlieToken = authenticationService.signin("c@b.c", "pwd");

        ChessGame game1 = chessGameController.createGame(charlieToken, alcibiade.getId(), bob.getId());
        Assertions.assertThat(game1.getWhitePlayer()).isEqualToComparingFieldByField(alcibiade);
        Assertions.assertThat(game1.getBlackPlayer()).isEqualToComparingFieldByField(bob);
        Assertions.assertThat(game1.getId().longValue()).isGreaterThan(0L);
        Assertions.assertThat(game1.getStartDate()).isInThePast();

        ChessGame game2 = chessGameController.getGame(game1.getId());
        Assertions.assertThat(game2).isEqualToComparingFieldByField(game1);
    }

    @Test(expected = ChessException.class)
    @Transactional
    public void testRefuseMove() {
        authenticationService.signup("a@b.c", "pwd", "Alcibiade");
        String alcibiadeToken = authenticationService.signin("a@b.c", "pwd");
        Player alcibiade = authenticationService.getSession(alcibiadeToken).getAccount().getPlayer();

        authenticationService.signup("b@b.c", "pwd", "Bob");
        String bobToken = authenticationService.signin("b@b.c", "pwd");
        Player bob = authenticationService.getSession(bobToken).getAccount().getPlayer();

        ChessGame game1 = chessGameController.createGame(alcibiadeToken, alcibiade.getId(), bob.getId());
        chessGameController.postMove(game1.getId(), alcibiadeToken, "e4");
        // Refuse 2nd move from same player
        chessGameController.postMove(game1.getId(), alcibiadeToken, "e5");
    }

}
