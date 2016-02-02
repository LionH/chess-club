package org.chesscorp.club.service;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.jobs.PgnImportProcessor;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.people.ClubPlayer;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.model.stats.ChessClubPosition;
import org.chesscorp.club.persistence.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigInteger;
import java.time.OffsetDateTime;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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

    @Autowired
    private PgnImportProcessor pgnImportProcessor;

    @Test
    @Transactional
    public void testIndexImportedGames() throws IOException {
        ClassPathResource cpr = new ClassPathResource("samples-pgn/McDonnell.pgn");

        pgnImportProcessor.process(cpr.getFile());
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
    @Transactional(propagation = Propagation.NEVER)
    public void testRelatedGames() throws Exception {
        Assertions.assertThat(chessPositionService.findRelatedGames(666L)).isEmpty();

        Player p1 = playerRepository.save(new ClubPlayer("Player 1"));
        Player p2 = playerRepository.save(new ClubPlayer("Player 2"));

        // Game 1

        ChessGame g1 = new ChessGame(p1, p2);
        chessGameRepository.save(g1);
        chessMoveRepository.save(g1.addMove(OffsetDateTime.now(), "e4"));
        chessMoveRepository.save(g1.addMove(OffsetDateTime.now(), "e5"));
        chessPositionService.updateMovePositions();

        Assertions.assertThat(chessPositionService.findRelatedGames(g1.getId())).isEmpty();
        Assertions.assertThat(chessPositionService.findRelatedGames(BigInteger.valueOf(g1.getId()))).isEmpty();

        // Game 2 - related to 1

        ChessGame g2 = new ChessGame(p1, p2);

        chessGameRepository.save(g2);
        chessPositionService.updateMovePositions();
        Assertions.assertThat(chessPositionService.findRelatedGames(g2.getId())).isEmpty();

        chessMoveRepository.save(g2.addMove(OffsetDateTime.now(), "e4"));
        chessPositionService.updateMovePositions();

        Assertions.assertThat(chessPositionService.findRelatedGames(g2.getId())).containsExactly(g1);

        // Game 3 - related to none

        ChessGame g3 = new ChessGame(p1, p2);

        chessGameRepository.save(g3);
        chessPositionService.updateMovePositions();
        Assertions.assertThat(chessPositionService.findRelatedGames(g3.getId())).isEmpty();

        chessMoveRepository.save(g3.addMove(OffsetDateTime.now(), "d4"));
        chessPositionService.updateMovePositions();
        Assertions.assertThat(chessPositionService.findRelatedGames(g3.getId())).isEmpty();
    }

    @Test
    @Transactional
    public void testPositionFindOrCreate() {
        String positionText1 = "bR-BQ-K-RPPP---PP--N--B-----P---q-----p----Pb----pp--n-pprn--k--r--kq-";
        String positionText2 = "wR-BQ-RK-PPPP--PP--N--N----B-Pp--------p---p-----pp-pqpbprnb-k-nr--kq-";
        String positionText3 = "wRNBQKB-RPPPP--PP-----N------Pp--------p---------pppp-p-prnbqkbnrKQkqg";

        Assertions.assertThat(chessPositionRepository.findAll()).hasSize(0);
        ChessClubPosition clubPosition1a = chessPositionService.findOrCreatePosition(positionText1);
        ChessClubPosition clubPosition1b = chessPositionService.findOrCreatePosition(positionText1);
        Assertions.assertThat(chessPositionRepository.findAll()).hasSize(1);
        Assertions.assertThat(clubPosition1a).isEqualToComparingFieldByField(clubPosition1b);

        ChessClubPosition clubPosition2 = chessPositionService.findOrCreatePosition(positionText2);
        Assertions.assertThat(clubPosition2.getId()).isPositive();
        Assertions.assertThat(clubPosition2.getText()).isEqualTo(positionText2);
        Assertions.assertThat(chessPositionRepository.findAll()).hasSize(2);

        ChessClubPosition clubPosition3 = chessPositionService.findOrCreatePosition(positionText3);
        Assertions.assertThat(clubPosition3.getId()).isPositive();
        Assertions.assertThat(clubPosition3.getText()).isEqualTo(positionText3);
        Assertions.assertThat(chessPositionRepository.findAll()).hasSize(3);
    }
}

