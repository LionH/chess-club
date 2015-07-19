package org.chesscorp.club.controllers;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.exception.ChessException;
import org.chesscorp.club.model.ChessGame;
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
    public void testGameCreation() {
        authenticationService.signup("a@b.c", "pwd", "Alcibiade");
        String alcibiadeToken = authenticationService.signin("a@b.c", "pwd");
        Player p1 = authenticationService.getPlayer(alcibiadeToken);

        authenticationService.signup("b@b.c", "pwd", "Bob");
        String bobToken = authenticationService.signin("b@b.c", "pwd");
        Player p2 = authenticationService.getPlayer(bobToken);

        ChessGame game1 = chessGameController.createGame(alcibiadeToken, p1.getId(), p2.getId());
        Assertions.assertThat(game1.getWhitePlayer()).isEqualToComparingFieldByField(p1);
        Assertions.assertThat(game1.getBlackPlayer()).isEqualToComparingFieldByField(p2);
        Assertions.assertThat(game1.getId()).isNotEmpty();
        Assertions.assertThat(game1.getStartDate()).isInThePast();

        ChessGame game2 = chessGameController.getGame(game1.getId());
        Assertions.assertThat(game2).isEqualToComparingFieldByField(game1);
    }

    @Test(expected = ChessException.class)
    @Transactional
    public void testRefuseThirdPartyCreation() {
        authenticationService.signup("a@b.c", "pwd", "Alcibiade");
        String alcibiadeToken = authenticationService.signin("a@b.c", "pwd");
        Player p1 = authenticationService.getPlayer(alcibiadeToken);

        authenticationService.signup("b@b.c", "pwd", "Bob");
        String bobToken = authenticationService.signin("b@b.c", "pwd");
        Player p2 = authenticationService.getPlayer(bobToken);

        authenticationService.signup("c@b.c", "pwd", "Charlie");
        String charlieToken = authenticationService.signin("c@b.c", "pwd");

        ChessGame game1 = chessGameController.createGame(charlieToken, p1.getId(), p2.getId());
        Assertions.assertThat(game1.getWhitePlayer()).isEqualToComparingFieldByField(p1);
        Assertions.assertThat(game1.getBlackPlayer()).isEqualToComparingFieldByField(p2);
        Assertions.assertThat(game1.getId()).isNotEmpty();
        Assertions.assertThat(game1.getStartDate()).isInThePast();

        ChessGame game2 = chessGameController.getGame(game1.getId());
        Assertions.assertThat(game2).isEqualToComparingFieldByField(game1);
    }

}
