package org.chesscorp.club.service;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
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
}
