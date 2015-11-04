package org.chesscorp.club.service;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.people.ClubPlayer;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.persistence.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@TransactionConfiguration(defaultRollback = true)
public class ChessPositionServiceTest {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ChessGameRepository chessGameRepository;

    @Autowired
    private ChessMoveRepository chessMoveRepository;

    @Autowired
    private ChessPositionRepository chessPositionRepository;

    @Autowired
    private ChessPositionService chessPositionService;

    @Autowired
    private ChessMoveToPositionRepository chessMoveToPositionRepository;

    @Autowired
    private ChessGameService chessGameService;

    @Test
    @Transactional
    public void testIndexImportedGames() throws IOException {
        ClassPathResource cpr = new ClassPathResource("samples-pgn/McDonnell.pgn");

        chessGameService.batchImport(cpr.getInputStream());
        Assertions.assertThat(playerRepository.count()).isEqualTo(8);
        Assertions.assertThat(chessGameRepository.count()).isEqualTo(106);
        Assertions.assertThat(chessMoveRepository.count()).isEqualTo(8434L);
        Assertions.assertThat(chessPositionRepository.count()).isEqualTo(0L);
        Assertions.assertThat(chessMoveToPositionRepository.count()).isEqualTo(0L);

        chessPositionService.updateMovePositions();
        Assertions.assertThat(playerRepository.count()).isEqualTo(8);
        Assertions.assertThat(chessGameRepository.count()).isEqualTo(106);
        Assertions.assertThat(chessMoveRepository.count()).isEqualTo(8434L);
        Assertions.assertThat(chessPositionRepository.count()).isEqualTo(7246L);
        Assertions.assertThat(chessMoveToPositionRepository.count()).isEqualTo(8434L);

        chessPositionService.updateMovePositions();
        Assertions.assertThat(playerRepository.count()).isEqualTo(8);
        Assertions.assertThat(chessGameRepository.count()).isEqualTo(106);
        Assertions.assertThat(chessMoveRepository.count()).isEqualTo(8434L);
        Assertions.assertThat(chessPositionRepository.count()).isEqualTo(7246L);
        Assertions.assertThat(chessMoveToPositionRepository.count()).isEqualTo(8434L);
    }

    @Test
    @Transactional
    public void testRelatedGames() throws Exception {
        Assertions.assertThat(chessPositionService.findRelatedGames(666L)).isEmpty();

        Player p1 = playerRepository.save(new ClubPlayer("Player 1"));
        Player p2 = playerRepository.save(new ClubPlayer("Player 2"));

        // Game 1

        ChessGame g1 = new ChessGame(p1, p2);
        chessGameRepository.save(g1);
        chessMoveRepository.save(g1.addMove(new Date(), "e4"));
        chessMoveRepository.save(g1.addMove(new Date(), "e5"));
        chessPositionService.updateMovePositions();

        Assertions.assertThat(chessPositionService.findRelatedGames(g1.getId())).isEmpty();
        Assertions.assertThat(chessPositionService.findRelatedGames(BigInteger.valueOf(g1.getId()))).isEmpty();

        // Game 2 - related to 1

        ChessGame g2 = new ChessGame(p1, p2);

        chessGameRepository.save(g2);
        chessPositionService.updateMovePositions();
        Assertions.assertThat(chessPositionService.findRelatedGames(g2.getId())).isEmpty();

        chessMoveRepository.save(g2.addMove(new Date(), "e4"));
        chessPositionService.updateMovePositions();

        Assertions.assertThat(chessPositionService.findRelatedGames(g2.getId())).containsExactly(g1);

        // Game 3 - related to none

        ChessGame g3 = new ChessGame(p1, p2);

        chessGameRepository.save(g3);
        chessPositionService.updateMovePositions();
        Assertions.assertThat(chessPositionService.findRelatedGames(g3.getId())).isEmpty();

        chessMoveRepository.save(g3.addMove(new Date(), "d4"));
        chessPositionService.updateMovePositions();
        Assertions.assertThat(chessPositionService.findRelatedGames(g3.getId())).isEmpty();
    }
}

